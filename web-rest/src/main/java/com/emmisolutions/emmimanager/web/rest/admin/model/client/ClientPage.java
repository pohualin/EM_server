package com.emmisolutions.emmimanager.web.rest.admin.model.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientsResource;
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
 * A HATEOAS wrapper for a page of ClientResource objects.
 */
@XmlRootElement(name = "client-page")
public class ClientPage extends PagedResource<ClientResource> {

    @XmlElement(name = "filter")
    private ClientSearchFilter searchFilter;

    public ClientPage() {
    }

    /**
     * Wrapper for client resource objects
     *
     * @param clientResourceSupports to be wrapped
     * @param clientPage             true page
     * @param filter                 which caused the response
     */
    public ClientPage(PagedResources<ClientResource> clientResourceSupports, Page<Client> clientPage, ClientSearchFilter filter) {
        pageDefaults(clientResourceSupports, clientPage);
        addFilterToLinks(filter);
    }

    /**
     * Create the search link
     *
     * @return Link for client searches
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.ClientsResource#list(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler, String, String)
     */
    public static Link createFullSearchLink() {
        Link link = linkTo(methodOn(ClientsResource.class).list(null, null, null, null, null)).withRel("clients");
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
     * Create a reference data link
     *
     * @return a reference data link
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.ClientsResource#getReferenceData()
     */
    public static Link createReferenceDataLink() {
        return linkTo(methodOn(ClientsResource.class).getReferenceData()).withRel("clientsReferenceData");
    }

    private void addFilterToLinks(ClientSearchFilter filter) {
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
