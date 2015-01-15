package com.emmisolutions.emmimanager.web.rest.admin.model.clientlocation;

import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.location.LocationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientLocationsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ClientLocationResource from a sparse ClientLocation. This
 * ResourceAssembler does not accept entities with null locations but does accept a null
 * entity id.
 */
@Component
public class ClientLocationFinderResourceAssembler implements ResourceAssembler<ClientLocation, ClientLocationResource> {

    @Resource
    ClientResourceAssembler clientResourceAssembler;

    @Resource
    LocationResourceAssembler locationResourceAssembler;

    @Override
    public ClientLocationResource toResource(ClientLocation entity) {
        ClientLocationResource ret = new ClientLocationResource();
        if (entity.getId() != null) {
            ret.add(linkTo(methodOn(ClientLocationsResource.class).view(entity.getClient().getId(), entity.getId())).withSelfRel());
        }
        ret.setClient(null);  // don't need client for relationship class
        ret.setLocation(locationResourceAssembler.toResource(entity.getLocation()));
        return ret;
    }

}
