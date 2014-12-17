package com.emmisolutions.emmimanager.web.rest.model.user.client.team;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.web.rest.resource.UserClientUserClientTeamRolesResource;

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
     * @return a Link to UserClientUserClientTeamResources - possible
     */
    public static Link createPossibleTeamsLink(UserClient userClient) {
	Link link = linkTo(
		methodOn(UserClientUserClientTeamRolesResource.class).possible(
			userClient.getId(), null, null, null, null, null))
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
				"status",
				TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
	return new Link(uriTemplate, link.getRel());
    }

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
