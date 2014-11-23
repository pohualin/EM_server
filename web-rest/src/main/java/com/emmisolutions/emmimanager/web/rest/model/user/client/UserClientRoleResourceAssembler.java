package com.emmisolutions.emmimanager.web.rest.model.user.client;

import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.web.rest.resource.RolesResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserClientRoleResource from a UserClientRole
 */
@Component
public class UserClientRoleResourceAssembler implements ResourceAssembler<UserClientRole, UserClientRoleResource>{

    @Override
    public UserClientRoleResource toResource(UserClientRole entity) {
        UserClientRoleResource ret = new UserClientRoleResource(entity);
        ret.add(linkTo(methodOn(RolesResource.class).get(entity.getId())).withSelfRel());
        ret.add(linkTo(methodOn(RolesResource.class).rolePermissions(entity.getId())).withRel("permissions"));
        return ret;
    }
}
