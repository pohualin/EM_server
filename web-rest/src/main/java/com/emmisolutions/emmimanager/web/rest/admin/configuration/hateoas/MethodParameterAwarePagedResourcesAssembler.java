package com.emmisolutions.emmimanager.web.rest.admin.configuration.hateoas;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

/**
 * Another monkey patch of a package visible resource with private final inaccessible variables.
 * Enhancement to the existing page/sort resolver that adds a prefix to the page links (e.g. page-xx)
 * as well as creates the explicit page number links (e.g. page-0, page-1, etc).
 */
class MethodParameterAwarePagedResourcesAssembler<T> extends PagedResourcesAssembler<T> {

    private final MethodParameter parameter;

    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver;
    private final UriComponents baseUri;

    /**
     * Creates a new {@link MethodParameterAwarePagedResourcesAssembler} using the given {@link MethodParameter},
     * {@link org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver} and base URI.
     *
     * @param parameter must not be {@literal null}.
     * @param resolver  can be {@literal null}.
     * @param baseUri   can be {@literal null}.
     */
    public MethodParameterAwarePagedResourcesAssembler(MethodParameter parameter,
                                                       HateoasPageableHandlerMethodArgumentResolver resolver, UriComponents baseUri) {

        super(resolver, baseUri);

        Assert.notNull(parameter, "Method parameter must not be null!");
        this.parameter = parameter;
        this.pageableResolver = resolver;
        this.baseUri = baseUri;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.web.PagedResourcesAssembler#getMethodParameter()
     */
    @Override
    protected MethodParameter getMethodParameter() {
        return parameter;
    }

    /**
     * Creates a resource from the page
     *
     * @param page      to assemble
     * @param assembler to use
     * @param <R>       the resource
     * @return the paged resource
     */
    public <R extends ResourceSupport> PagedResources<R> toResource(Page<T> page, ResourceAssembler<T, R> assembler) {
        return createResource(page, assembler, null);
    }

    /**
     * Creates a resource from the page
     *
     * @param page      to assemble
     * @param assembler to use
     * @param link      to use
     * @param <R>       resource
     * @return the paged resource
     */
    public <R extends ResourceSupport> PagedResources<R> toResource(Page<T> page, ResourceAssembler<T, R> assembler,
                                                                    Link link) {

        Assert.notNull(link, "Link must not be null!");
        return createResource(page, assembler, link);
    }

    private <S, R extends ResourceSupport> PagedResources<R> createResource(Page<S> page,
                                                                            ResourceAssembler<S, R> assembler, Link link) {

        Assert.notNull(page, "Page can not be null!");
        Assert.notNull(assembler, "ResourceAssembler must not be null!");

        List<R> resources = new ArrayList<>(page.getNumberOfElements());

        for (S element : page) {
            resources.add(assembler.toResource(element));
        }

        PagedResources<R> pagedResources = new PagedResources<>(resources, asPageMetadata(page));
        return addPaginationLinks(pagedResources, page, link == null ? getDefaultUriString().toUriString() : link.getHref());
    }

    private UriComponents getDefaultUriString() {
        return baseUri == null ? getBuilder().build() : baseUri;
    }

    private static UriComponentsBuilder getBuilder() {

        HttpServletRequest request = getCurrentRequest();
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromRequestUri(request);

        String forwardedSsl = request.getHeader("X-Forwarded-Ssl");

        if (StringUtils.hasText(forwardedSsl) && forwardedSsl.equalsIgnoreCase("on")) {
            builder.scheme("https");
        }

        String host = request.getHeader("X-Forwarded-Host");

        if (!StringUtils.hasText(host)) {
            return builder;
        }

        String[] hosts = StringUtils.commaDelimitedListToStringArray(host);
        String hostToUse = hosts[0];

        if (hostToUse.contains(":")) {

            String[] hostAndPort = StringUtils.split(hostToUse, ":");

            builder.host(hostAndPort[0]);
            builder.port(Integer.parseInt(hostAndPort[1]));

        } else {
            builder.host(hostToUse);
            builder.port(-1); // reset port if it was forwarded from default port
        }

        String port = request.getHeader("X-Forwarded-Port");

        if (StringUtils.hasText(port)) {
            builder.port(Integer.parseInt(port));
        }

        return builder;
    }

    private static HttpServletRequest getCurrentRequest() {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }

    private <R extends ResourceSupport> PagedResources<R> addPaginationLinks(PagedResources<R> resources, Page<?> page,
                                                                             String uri) {
        String prefix = "page-";

        Pageable first = null;
        Pageable current = null;
        Pageable previous = null;
        if (page.hasNext()) {
            Pageable next = page.nextPageable();
            first = next.first();
            current = next.previousOrFirst();
        }

        if (page.hasPrevious()) {
            previous = page.previousPageable();
            first = previous.first();
            current = previous.next();
        }
        List<Pageable> previous5Pages = new ArrayList<>();
        List<Pageable> next5Pages = new ArrayList<>();
        boolean atLastPage = false;
        // removed first link
//        boolean firstPageIsInPrevious = current != null && !current.hasPrevious();
        if (current != null) {
            // add 5 links back
            Pageable startingBack = current;
            while (startingBack.hasPrevious() && previous5Pages.size() < 5) {
                startingBack = startingBack.previousOrFirst();
                previous5Pages.add(0, startingBack);
//                firstPageIsInPrevious = !startingBack.hasPrevious();
            }

            // add 5 links forward
            int count = 0;
            int forwardMax = (current.equals(first) ? 10 : 11) - previous5Pages.size();
            Pageable next = current;
            while (!atLastPage && count < forwardMax) {
                count++;
                next5Pages.add(next);
                next = next.next();
                atLastPage = next.getPageNumber() >= page.getTotalPages();
            }

        }

        // write the actual resource links
        if (current != null) {
            foo(resources, current, uri, prefix + Link.REL_SELF);
        }
        // add previous link always, first link when the first page isn't already a page-xxx link
        if (current != null && current.hasPrevious()) {
//            if (!firstPageIsInPrevious) {
//                foo(resources, first, uri, prefix + Link.REL_FIRST);
//            }
            foo(resources, previous, uri, prefix + Link.REL_PREVIOUS);
        }
        // add the past five pages
        for (Pageable previous5Page : previous5Pages) {
            foo(resources, previous5Page, uri, prefix + (previous5Page.getPageNumber() + 1));
        }
        // add the next five pages
        for (Pageable next5Page : next5Pages) {
            foo(resources, next5Page, uri, prefix + (next5Page.getPageNumber() + 1));
        }
        // add the next link, if there is a next page
        if (page.hasNext()) {
            foo(resources, page.nextPageable(), uri, prefix + Link.REL_NEXT);
        }
        // add last link when the links don't already have the last page as a page-xxx link
//        if (!atLastPage && current != null) {
//            Pageable toTheEnd = current;
//            while (!atLastPage) {
//                toTheEnd = toTheEnd.next();
//                atLastPage = toTheEnd.getPageNumber() >= page.getTotalPages();
//            }
//            foo(resources, toTheEnd.previousOrFirst(), uri, prefix + Link.REL_LAST);
//        }

        resources.add(appendPaginationParameterTemplates(new Link(uri)));

        return resources;
    }

    private void foo(PagedResources<?> resources, Pageable pageable, String uri, String rel) {

        UriComponentsBuilder builder = fromUriString(uri);
        pageableResolver.enhance(builder, getMethodParameter(), pageable);
        resources.add(new Link(builder.build().toUriString(), rel));
    }

    private static <T> PagedResources.PageMetadata asPageMetadata(Page<T> page) {

        Assert.notNull(page, "Page must not be null!");
        return new PagedResources.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
    }
}

