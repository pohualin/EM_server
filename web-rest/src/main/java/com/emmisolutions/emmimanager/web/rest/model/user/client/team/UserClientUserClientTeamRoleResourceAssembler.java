package com.emmisolutions.emmimanager.web.rest.model.user.client.team;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;

/**
 * Creates a UserClientTeamRoleResource from a UserClientTeamRole
 */
@Component
public class UserClientUserClientTeamRoleResourceAssembler
	implements
	ResourceAssembler<UserClientUserClientTeamRole, UserClientUserClientTeamRoleResource> {

    @Override
    public UserClientUserClientTeamRoleResource toResource(
	    UserClientUserClientTeamRole entity) {
	UserClientUserClientTeamRoleResource ret = new UserClientUserClientTeamRoleResource(
		entity);
	return ret;
    }
}
