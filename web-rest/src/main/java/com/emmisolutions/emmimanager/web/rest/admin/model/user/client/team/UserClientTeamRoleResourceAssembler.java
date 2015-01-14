package com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientTeamRolesAdminResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserClientTeamRoleResource from a UserClientTeamRole
 */
@Component
public class UserClientTeamRoleResourceAssembler implements ResourceAssembler<UserClientTeamRole, UserClientTeamRoleResource>{

    @Override
    public UserClientTeamRoleResource toResource(UserClientTeamRole entity) {
        UserClientTeamRoleResource ret = new UserClientTeamRoleResource(entity);
        ret.add(linkTo(methodOn(ClientTeamRolesAdminResource.class).get(entity.getId())).withSelfRel());
        ret.add(linkTo(methodOn(ClientTeamRolesAdminResource.class).rolePermissions(entity.getId())).withRel("permissions"));
        return ret;
    }
}
