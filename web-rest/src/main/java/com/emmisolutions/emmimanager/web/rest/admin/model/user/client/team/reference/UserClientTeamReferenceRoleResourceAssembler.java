package com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.reference;

import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRole;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Responsible for creating a UserClientTeamReferenceRoleResource from a UserClientReferenceTeamRole
 */
@Component
public class UserClientTeamReferenceRoleResourceAssembler implements
    ResourceAssembler<UserClientReferenceTeamRole, UserClientTeamReferenceRoleResource> {

    @Override
    public UserClientTeamReferenceRoleResource toResource(UserClientReferenceTeamRole entity) {
        return new UserClientTeamReferenceRoleResource(entity);
    }
}
