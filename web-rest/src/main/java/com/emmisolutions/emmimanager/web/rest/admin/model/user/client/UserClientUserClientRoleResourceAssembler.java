package com.emmisolutions.emmimanager.web.rest.admin.model.user.client;

import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientRolesAdminResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.UserClientUserClientRolesResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserClientUserClientRoleResource from a UserClientUserClientRole
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
        ret.add(linkTo(
                methodOn(UserClientUserClientRolesResource.class)
                        .getUserClientUserClientRole(entity.getId())).withRel(
                "userClientUserClientRole"));
        ret.setEntity(entity);
        return ret;
    }

}
