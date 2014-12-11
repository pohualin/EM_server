package com.emmisolutions.emmimanager.web.rest.model.user.client;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.web.rest.resource.ClientRolesAdminResource;

/**
 * Creates a UserResource from a User
 */
@Component
public class UserClientUserClientRoleResourceAssembler
	implements
	ResourceAssembler<UserClientUserClientRole, UserClientUserClientRoleResource> {

    @Override
    public UserClientUserClientRoleResource toResource(
	    UserClientUserClientRole entity) {
	UserClientUserClientRoleResource ret = new UserClientUserClientRoleResource();
	ret.add(linkTo(
		methodOn(ClientRolesAdminResource.class).rolePermissions(
			entity.getUserClientRole().getId())).withRel(
		"userClientRolePermissions"));
	ret.setEntity(entity);
	return ret;
    }

}
