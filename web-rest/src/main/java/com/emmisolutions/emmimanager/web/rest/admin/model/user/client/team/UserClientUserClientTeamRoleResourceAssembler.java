package com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.web.rest.admin.resource.UserClientUserClientTeamRolesResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
        if (entity.getId() != null) {
            ret.add(linkTo(
                    methodOn(UserClientUserClientTeamRolesResource.class)
                            .getUserClientUserClientTeamRole(entity.getId()))
                    .withSelfRel());
        }
        return ret;
    }

    /**
     * Link to createPossibleTeamsLink
     * 
     * @param userClient
     *            to use
     * @return a Link to UserClientUserClientTeamResources - possible
     */
    public static Link createPossibleTeamsLink(UserClient userClient) {
        Link link = linkTo(
                methodOn(UserClientUserClientTeamRolesResource.class).possible(
                        userClient.getId(), null, null, null, null, null, null))
                .withRel("possibleTeams");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page",
                                TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable(
                                "size",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(
                                "sort",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(
                                "term",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(
                                "tagId",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(
                                "status",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));

        return new Link(uriTemplate, link.getRel());
    }

    /**
     * Link to getUserClientUserClientTeamRoles
     * 
     * @param userClient
     *            to use
     * @return a Link to getUserClientUserClientTeamRoles
     */
    public static Link createGetUserClientUserClientTeamRolesLink(
            UserClient userClient) {
        Link link = linkTo(
                methodOn(UserClientUserClientTeamRolesResource.class)
                        .getUserClientUserClientTeamRoles(userClient.getId(),
                                null, null, null, null)).withRel(
                "existingTeams");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page",
                                TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable(
                                "sort",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(
                                "userClientTeamRoleId",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }
}
