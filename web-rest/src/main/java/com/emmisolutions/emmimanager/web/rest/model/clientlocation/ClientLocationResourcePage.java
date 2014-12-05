package com.emmisolutions.emmimanager.web.rest.model.clientlocation;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.ClientLocationsResource;
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
 * A HATEOAS wrapper for a page of ClientLocationResource objects.
 */
@XmlRootElement(name = "client-location-page")
public class ClientLocationResourcePage extends PagedResource<ClientLocationResource> {

    @XmlElement(name = "filter")
    private LocationSearchFilter searchFilter;

    public ClientLocationResourcePage() {
    }

    /**
     * Constructor that sets up paging defaults
     *
     * @param clientLocationResourceSupports page of ClientLocationResource objects
     * @param clientLocationPage             page of ClientLocation objects
     * @param filter                         the filter
     */
    public ClientLocationResourcePage(PagedResources<ClientLocationResource> clientLocationResourceSupports,
                                      Page<ClientLocation> clientLocationPage, LocationSearchFilter filter) {
        pageDefaults(clientLocationResourceSupports, clientLocationPage);
        if (filter != null) {
            addFilterToLinks(filter);
        }
    }

    /**
     * Create the search link
     *
     * @param client the client
     * @return Link for location searches
     * @see com.emmisolutions.emmimanager.web.rest.resource.ClientLocationsResource#possible(Long, org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler, String, String)
     */
    public static Link createAssociationLink(Client client) {
        Link link = linkTo(methodOn(ClientLocationsResource.class).possible(client.getId(), null, null, null, null, null)).withRel("possibleLocations");
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
     * Creates a link loading associations not at a client
     *
     * @param client the client
     * @return the link
     */
    public static Link createAssociationWLink(Client client) {
        Link link = linkTo(methodOn(ClientLocationsResource.class).possibleWithoutClientLocations(client.getId(), null, null, null, null, null)).withRel("possibleLocationsWithoutCL");
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
     * This is the link to find current locations on a client
     *
     * @param client on which to find current locations
     * @return the link
     */
    public static Link createCurrentLocationsSearchLink(Client client) {
        Link link = linkTo(methodOn(ClientLocationsResource.class).current(client.getId(), null, null, null)).withRel("locations");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(LocationSearchFilter filter) {
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
