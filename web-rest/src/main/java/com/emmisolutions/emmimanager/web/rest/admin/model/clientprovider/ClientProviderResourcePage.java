package com.emmisolutions.emmimanager.web.rest.admin.model.clientprovider;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientLocationsResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientProvidersResource;

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
public class ClientProviderResourcePage extends PagedResource<ClientProviderResource> {

    @XmlElement(name = "filter")
    private ProviderSearchFilter searchFilter;

    public ClientProviderResourcePage() {
    }

    /**
     * Constructor that sets up paging defaults
     *
     * @param clientProviderResourceSupports page of ClientProviderResource objects
     * @param clientProviderPage             page of ClientProvider objects
     * @param filter                         the filter
     */
    public ClientProviderResourcePage(PagedResources<ClientProviderResource> clientProviderResourceSupports,
                                      Page<ClientProvider> clientProviderPage, ProviderSearchFilter filter) {
        pageDefaults(clientProviderResourceSupports, clientProviderPage);
        if (filter != null) {
            addFilterToLinks(filter);
        }
    }

    /**
     * Create the search link
     *
     * @param client to find within
     * @return Link for provider searches
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.ClientProvidersResource#possible(Long, org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler, String, String)
     */
    public static Link createAssociationLink(Client client) {
        Link link = linkTo(methodOn(ClientProvidersResource.class).possible(client.getId(), null, null, null, null, null)).withRel("possibleProviders");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    /**
     * This is the link to find current providers on a client
     *
     * @param client on which to find current providers
     * @return the link
     */
    public static Link createCurrentProvidersSearchLink(Client client) {
        Link link = linkTo(methodOn(ClientProvidersResource.class).current(client.getId(), null, null, null)).withRel("providers");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }
    
    /**
     * Creates a link loading associations not at a client
     *
     * @param client the client
     * @return the link
     */
    public static Link createPossibleProvidersNotUseingGivenClientLink(Client client) {
        Link link = linkTo(methodOn(ClientProvidersResource.class).possibleWithoutClientProviders(client.getId(), null, null, null, null, null)).withRel("possibleProvidersWithoutCL");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
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
