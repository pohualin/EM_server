package com.emmisolutions.emmimanager.web.rest.model.user;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;

/**
 * Creates a UserAdminRoleResourceAssembler from a UserAdminRole
 *
 */
@Component("userAdminRoleResourceAssembler")
public class UserAdminRoleResourceAssembler implements ResourceAssembler<UserAdminRole, UserAdminRoleResource> {

    @Override
    public UserAdminRoleResource toResource(UserAdminRole role) {
    	UserAdminRoleResource ret = new UserAdminRoleResource();
    	ret.setEntity(role);
    	return ret;
    }

}
