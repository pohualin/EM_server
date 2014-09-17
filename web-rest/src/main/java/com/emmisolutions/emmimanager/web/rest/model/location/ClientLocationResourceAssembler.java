package com.emmisolutions.emmimanager.web.rest.model.location;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.web.rest.resource.LocationsResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a LocationResource (which has links) from a Location (entity)
 */

public class ClientLocationResourceAssembler implements ResourceAssembler<Location, LocationResource> {

    private Long clientId;

    public ClientLocationResourceAssembler(Long clientId){
        this.clientId = clientId;
    }

    @Override
    public LocationResource toResource(Location entity) {
        LocationResource ret = new LocationResource();
        ret.add(createLocationAtClientLink(clientId, entity));
        ret.setEntity(entity);
        return ret;
    }

    public static Link createLocationAtClientLink(Long clientId, Location location){
        return linkTo(methodOn(LocationsResource.class).getLocationAtClient(clientId, location.getId())).withRel("self");
    }
}
