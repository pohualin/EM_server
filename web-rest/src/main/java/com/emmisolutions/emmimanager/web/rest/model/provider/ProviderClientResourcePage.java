package com.emmisolutions.emmimanager.web.rest.model.provider;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.ProvidersResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of ClientProviderResource objects.
 */
@XmlRootElement(name = "client-provider-page")
public class ProviderClientResourcePage extends PagedResource<ProviderClientResource> {

    @XmlElement(name = "filter")
    private ProviderSearchFilter searchFilter;

    public ProviderClientResourcePage() {
    }

    /**
     * Constructor that sets up paging defaults
     *
     * @param clientProviderResourceSupports page of ClientProviderResource objects
     * @param clientProviderPage             page of ClientProvider objects
     * @param filter                         the filter
     */
    public ProviderClientResourcePage(PagedResources<ProviderClientResource> clientProviderResourceSupports,
                                      Page<ClientProvider> clientProviderPage, ProviderSearchFilter filter) {
        pageDefaults(clientProviderResourceSupports, clientProviderPage);
        if (filter != null) {
            addFilterToLinks(filter);
        }
    }

    /**
     * This is the link to find current clients on a provider
     *
     * @param provider on which to find current clients
     * @return the link
     */
    public static Link createCurrentClientsSearchLink(Provider provider) {
        Link link = linkTo(methodOn(ProvidersResource.class).currentClients(provider.getId(), null, null, null)).withRel("clients");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
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
