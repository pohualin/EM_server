package com.emmisolutions.emmimanager.web.rest.model.user.client;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.resource.UserClientUserClientRolesResource;
import com.emmisolutions.emmimanager.web.rest.resource.UsersClientResource;

/**
 * Creates a UserResource from a User
 */
@Component
public class UserClientResourceAssembler implements
	ResourceAssembler<UserClient, UserClientResource> {

    @Override
    public UserClientResource toResource(UserClient entity) {
	UserClientResource ret = new UserClientResource();
	ret.add(linkTo(methodOn(UsersClientResource.class).get(entity.getId()))
		.withSelfRel());
	ret.add(linkTo(
		methodOn(UserClientUserClientRolesResource.class)
			.getUserClientUserClientRoles(entity.getId(), null,
				null, null)).withRel("userClientRoles"));
	ret.setEntity(entity);
	return ret;
    }
}
