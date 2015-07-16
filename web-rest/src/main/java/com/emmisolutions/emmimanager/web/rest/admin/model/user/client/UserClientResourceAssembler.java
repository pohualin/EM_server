package com.emmisolutions.emmimanager.web.rest.admin.model.user.client;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.UserClientUserClientTeamRoleResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.resource.CasesResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.UserClientPasswordResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.UserClientUserClientRolesResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.UserClientsResource;
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
                                null)).withRel("userClientRoles"));
        ret.add(UserClientUserClientTeamRoleResourceAssembler
                .createPossibleTeamsLink(entity));
        ret.add(UserClientUserClientTeamRoleResourceAssembler
                .createGetUserClientUserClientTeamRolesLink(entity));
        ret.add(linkTo(methodOn(UserClientPasswordResource.class)
                .set(entity.getId(), null)).withRel("changePassword"));
        ret.add(linkTo(methodOn(UserClientsResource.class)
                .activate(entity.getId())).withRel("activate"));
        ret.add(linkTo(methodOn(UserClientsResource.class)
                .resetPassword(entity.getId())).withRel("resetPassword"));
        ret.add(linkTo(methodOn(CasesResource.class).referenceData(entity.getId())).withRel("createCase"));
        ret.setEntity(entity);
        return ret;
    }
}
