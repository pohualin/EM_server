package com.emmisolutions.emmimanager.web.rest.model.user.client;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;

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
	ret.setEntity(entity);
	return ret;
    }

}
