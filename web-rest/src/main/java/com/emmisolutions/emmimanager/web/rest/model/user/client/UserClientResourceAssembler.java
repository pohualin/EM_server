package com.emmisolutions.emmimanager.web.rest.model.user.client;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.resource.UserClientUserClientRolesResource;
import com.emmisolutions.emmimanager.web.rest.resource.UserClientsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserClientResource from a UserClient
 */
@Component
public class UserClientResourceAssembler implements
        ResourceAssembler<UserClient, UserClientResource> {

    @Override
    public UserClientResource toResource(UserClient entity) {
        UserClientResource ret = new UserClientResource();
        ret.add(linkTo(methodOn(UserClientsResource.class).get(entity.getId()))
                .withSelfRel());
        ret.add(linkTo(
                methodOn(UserClientUserClientRolesResource.class)
                        .getUserClientUserClientRoles(entity.getId(), null,
                                null, null)).withRel("userClientRoles"));
        ret.setEntity(entity);
        return ret;
    }
}
