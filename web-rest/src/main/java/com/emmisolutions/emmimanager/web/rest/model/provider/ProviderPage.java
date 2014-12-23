package com.emmisolutions.emmimanager.web.rest.model.provider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.ProvidersResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamProvidersResource;

/**
 * A HATEOAS wrapper for a page of ProviderResource objects.
 */
@XmlRootElement(name = "provider-page")
public class ProviderPage extends PagedResource<ProviderResource> {


    @XmlElement(name = "filter")
    private ProviderSearchFilter searchFilter;

    public ProviderPage() {
    }

    /**
     * Wrapped constructor
     *
     * @param providerResources to be wrapped
     * @param providerPage      the raw response
     * @param filter            the filter
     */
    public ProviderPage(PagedResources<ProviderResource> providerResources, Page<Provider> providerPage, ProviderSearchFilter filter) {
        pageDefaults(providerResources, providerPage);
        if (filter != null) {
            addFilterToLinks(filter);
        }
    }

    /**
     * Link for ref data (Specialty types) for providers
     *
     * @return Link reference data for providers
     */
    public static Link createProviderReferenceDataLink() {
        Link link = linkTo(methodOn(ProvidersResource.class).getRefData(null, null, null)).withRel("providerReferenceData");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
            ));
        return new Link(uriTemplate, link.getRel());
    }

    /**
     * Link for Create Provider
     *
     * @param clientId
     * @param teamId
     * @return Link for create provider
     */
    public static Link createProviderLink(Long clientId, Long teamId) {
        Link link = linkTo(methodOn(ProvidersResource.class).create(null, teamId, clientId)).withRel("provider");
        UriTemplate uriTemplate = new UriTemplate(link.getHref());
        return new Link(uriTemplate, link.getRel());
    }

    /**
     * Link for provider search
     *
     * @return Link for provider search
     */
    public static Link createProviderFullSearchLink() {
        Link link = linkTo(methodOn(ProvidersResource.class).list(null, null, null, null, null)).withRel("providers");
        UriTemplate uriTemplate = new UriTemplate(link.getHref()).with(
            new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(ProviderSearchFilter filter) {
        this.searchFilter = filter;
        if (CollectionUtils.isEmpty(links)) {
            return;
        }
        // re-write the links to include the filters
        List<Link> existingLinks = links;
        this.links = new ArrayList<>();
        for (Link link : existingLinks) {
            String rel = link.getRel();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(link.getHref());
            if (link.isTemplated()) {
                // add args to template
                UriTemplate uriTemplate = new UriTemplate(link.getHref())
                    .with(new TemplateVariables(
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values
                if (filter.getStatus() != null) {
                    builder.queryParam("status", filter.getStatus());
                }
                if (!CollectionUtils.isEmpty(filter.getNames())) {
                    for (String s : filter.getNames()) {
                        builder.queryParam("name", s);
                    }
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }

}
