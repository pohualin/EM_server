package com.emmisolutions.emmimanager.web.rest.model.user.client.team;

import java.util.ArrayList;
import java.util.List;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.UserClientUserClientTeamRoleSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.ClientTeamRolesAdminResource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A Page of UserClientUserClientRoleTeamResource objects
 */
public class UserClientUserClientTeamRoleResourcePage extends
		PagedResource<UserClientUserClientTeamRoleResource> {

    private UserClientUserClientTeamRoleSearchFilter filter;
    
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
			Page<UserClientUserClientTeamRole> userClientUserClientTeamRolePage, UserClientUserClientTeamRoleSearchFilter filter) {
		pageDefaults(userClientUserClientTeamRoleResource,
				userClientUserClientTeamRolePage);
        if (filter != null) {
            addFilterToLinks(filter);
        }
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
	
    /**
     * Add filter to page links
     * 
     * @param filter
     *            to use
     */
    private void addFilterToLinks(
            UserClientUserClientTeamRoleSearchFilter filter) {
        this.filter = filter;
        if (CollectionUtils.isEmpty(links)) {
            return;
        }
        List<Link> existingLinks = links;
        this.links = new ArrayList<>();
        for (Link link : existingLinks) {
            String rel = link.getRel();
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(link.getHref());
            if (link.isTemplated()) {
                // add args to template
                UriTemplate uriTemplate = new UriTemplate(link.getHref())
                        .with(new TemplateVariables(
                                new TemplateVariable(
                                        "term",
                                        TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(
                                        "tagId",
                                        TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                                new TemplateVariable(
                                        "status",
                                        TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values
                if (filter.getStatus() != null) {
                    builder.queryParam("status", filter.getStatus());
                }
                if (StringUtils.isNotBlank(filter.getTerm())) {
                    builder.queryParam("term", filter.getTerm());
                }
                if (filter.getTag() != null) {
                    builder.queryParam("tagId", filter.getTag().getId());
                }
                this.links.add(new Link(builder.build().encode().toUriString(),
                        rel));
            }
        }
    }

}
