package com.emmisolutions.emmimanager.web.rest.model.location;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.web.rest.resource.LocationsResource;

/**
 * Responsible for creating a LocationResource (which has links) from a Location (entity)
 */
@Component
public class LocationResourceAssembler implements ResourceAssembler<Location, LocationResource> {

    @Override
    public LocationResource toResource(Location entity) {
        LocationResource ret = new LocationResource();
        ret.add(linkTo(methodOn(LocationsResource.class).get(entity.getId())).withSelfRel());
        ret.add(createCurrentClientsSearchLink(entity));
        ret.setEntity(entity);
        return ret;
    }
    
	/**
	 * This is the link to find current clients on a location
	 * 
	 * @param location
	 *            on which to find current clients
	 * @return the link
	 */
	public static Link createCurrentClientsSearchLink(Location location) {
		Link link = linkTo(
				methodOn(LocationsResource.class).currentClients(
						location.getId(), null, null, null)).withRel("clients");
		UriTemplate uriTemplate = new UriTemplate(link.getHref())
				.with(new TemplateVariables(
						new TemplateVariable("page",
								TemplateVariable.VariableType.REQUEST_PARAM),
						new TemplateVariable(
								"size",
								TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
						new TemplateVariable(
								"sort",
								TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
		return new Link(uriTemplate, link.getRel());
	}

}
