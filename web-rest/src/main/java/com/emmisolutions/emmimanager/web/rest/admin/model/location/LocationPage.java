package com.emmisolutions.emmimanager.web.rest.admin.model.location;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.LocationsResource;
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
 * A HATEOAS wrapper for a page of LocationResource objects.
 */
@XmlRootElement(name = "location-page")
public class LocationPage extends PagedResource<LocationResource> {

    @XmlElement(name = "filter")
    private LocationSearchFilter searchFilter;

    public LocationPage() {
    }

    /**
     * Creates a page for a location search result
     *
     * @param locationResourceSupports to be wrapped
     * @param locationPage             true page
     * @param filter                   which caused the response
     */
    public LocationPage(PagedResources<LocationResource> locationResourceSupports, Page<Location> locationPage, LocationSearchFilter filter) {
        pageDefaults(locationResourceSupports, locationPage);
        addFilterToLinks(filter);
    }

    /**
     * Create a reference data link
     *
     * @return the reference data link
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.LocationsResource#getReferenceData()
     */
    public static Link createReferenceDataLink() {
        return linkTo(methodOn(LocationsResource.class).getReferenceData()).withRel("locationReferenceData");
    }

    /**
     * Create the search link
     *
     * @return Link for location searches
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.LocationsResource#list(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, String, org.springframework.data.web.PagedResourcesAssembler, String...)
     */
    public static Link createFullSearchLink() {
        Link link = linkTo(methodOn(LocationsResource.class).list(null, null, null, null, (String[]) null)).withRel("locations");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
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
