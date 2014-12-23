package com.emmisolutions.emmimanager.web.rest.model.user.client.team;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.ClientTeamRolesAdminResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A Page of UserClientUserClientRoleTeamResource objects
 */
public class UserClientUserClientTeamRoleResourcePage extends
		PagedResource<UserClientUserClientTeamRoleResource> {

	public UserClientUserClientTeamRoleResourcePage() {
	}

	/**
	 * Wrapped constructor
	 *
	 * @param userClientUserClientTeamRoleResource
	 *            to be wrapped
	 * @param userClientUserClientTeamRolePage
	 *            the raw response
	 */
	public UserClientUserClientTeamRoleResourcePage(
			PagedResources<UserClientUserClientTeamRoleResource> userClientUserClientTeamRoleResource,
			Page<UserClientUserClientTeamRole> userClientUserClientTeamRolePage) {
		pageDefaults(userClientUserClientTeamRoleResource,
				userClientUserClientTeamRolePage);
	}

	/**
	 * Create the search link
	 *
	 * @param client
	 *            the client
	 * @return Link for team roles on a client
	 * @see com.emmisolutions.emmimanager.web.rest.resource.ClientTeamRolesAdminResource#clientTeamRoles(Long,
	 *      org.springframework.data.domain.Pageable,
	 *      org.springframework.data.web.PagedResourcesAssembler)
	 */
	public static Link createFullSearchLink(Client client) {
		Link link = linkTo(
				methodOn(ClientTeamRolesAdminResource.class).clientTeamRoles(
						client.getId(), null, null)).withRel("teamRoles");
		UriTemplate uriTemplate = new UriTemplate(link.getHref())
				.with(new TemplateVariables(
						new TemplateVariable("page",
								TemplateVariable.VariableType.REQUEST_PARAM),
						new TemplateVariable(
								"size",
								TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
						new TemplateVariable(
								"sort",
								TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
		return new Link(uriTemplate, link.getRel());
	}

}
