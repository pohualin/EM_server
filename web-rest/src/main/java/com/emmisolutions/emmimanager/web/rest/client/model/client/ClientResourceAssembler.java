package com.emmisolutions.emmimanager.web.rest.client.model.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ClientResource (which has links) from a Client
 */
@Component("userClientClientResourceAssembler")
public class ClientResourceAssembler implements ResourceAssembler<Client, ClientResource> {

    @Override
    public ClientResource toResource(Client entity) {
        if (entity == null) {
            return null;
        }
        ClientResource ret = new ClientResource();
        ret.add(linkTo(methodOn(ClientsResource.class).get(entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}