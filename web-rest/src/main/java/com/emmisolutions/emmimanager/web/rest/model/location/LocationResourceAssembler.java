package com.emmisolutions.emmimanager.web.rest.model.location;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.web.rest.resource.LocationsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a LocationResource (which has links) from a Location (entity)
 */
@Component
public class LocationResourceAssembler implements ResourceAssembler<Location, LocationResource> {

    @Override
    public LocationResource toResource(Location entity) {
        LocationResource ret = new LocationResource();
        ret.add(linkTo(methodOn(LocationsResource.class).get(entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}
