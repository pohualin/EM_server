package com.emmisolutions.emmimanager.web.rest.model.clientlocation;

import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.resource.ClientLocationsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ClientLocationResource
 */
@Component
public class ClientLocationResourceAssembler implements ResourceAssembler<ClientLocation, ClientLocationResource> {

    @Resource
    ClientResourceAssembler clientResourceAssembler;

    @Resource
    LocationResourceAssembler locationResourceAssembler;

    @Override
    public ClientLocationResource toResource(ClientLocation entity) {
        ClientLocationResource ret = new ClientLocationResource();
        ret.add(linkTo(methodOn(ClientLocationsResource.class).view(entity.getClient().getId(), entity.getId())).withSelfRel());
        ret.setClient(null); // don't need to send up the client
        ret.setLocation(locationResourceAssembler.toResource(entity.getLocation()));
        return ret;
    }

}
