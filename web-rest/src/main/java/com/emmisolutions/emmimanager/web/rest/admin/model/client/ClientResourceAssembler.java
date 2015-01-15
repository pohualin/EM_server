package com.emmisolutions.emmimanager.web.rest.admin.model.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.admin.model.clientlocation.ClientLocationResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.clientprovider.ClientProviderResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.groups.GroupPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamTagPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.UserClientRoleResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.UserClientTeamRoleResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.resource.*;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ClientResource (which has links) from a Client
 */
@Component
public class ClientResourceAssembler implements ResourceAssembler<Client, ClientResource> {

    @Override
    public ClientResource toResource(Client entity) {
        ClientResource ret = new ClientResource();
        ret.add(linkTo(methodOn(ClientsResource.class).get(entity.getId())).withSelfRel());
        ret.add(TeamResource.clientTeams(entity));
        ret.add(GroupPage.createFullSearchLink(entity));
        ret.add(linkTo(methodOn(GroupsResource.class).invalidTeams(null, entity.getId())).withRel("invalidTeams"));
        ret.add(TeamPage.createFindTeamByNormalizedNameLink(entity));
        ret.add(TeamResource.createTeamByTeamIdLink(entity));
        ret.add(ClientLocationResourcePage.createCurrentLocationsSearchLink(entity));
        ret.add(ClientLocationResourcePage.createAssociationLink(entity));
        ret.add(ClientLocationResourcePage.createAssociationWLink(entity));
        ret.add(ClientProviderResourcePage.createCurrentProvidersSearchLink(entity));
        ret.add(ClientProviderResourcePage.createAssociationLink(entity));
        ret.add(ProviderPage.createProviderReferenceDataLink());
        ret.add(UserClientRoleResourcePage.createFullSearchLink(entity));
        ret.add(linkTo(methodOn(ClientRolesAdminResource.class).reference()).withRel("rolesReferenceData"));
        ret.add(UserClientTeamRoleResourcePage.createFullSearchLink(entity));
        ret.add(linkTo(methodOn(ClientTeamRolesAdminResource.class).referenceData()).withRel("teamRolesReferenceData"));
        ret.add(TeamTagPage.createFullSearchLinkTeamTagsWithTags(entity));
        ret.add(createFullUsersSearchLink(entity));
        ret.setEntity(entity);
        return ret;
    }

    /**
     * Create the search link
     *
     * @return Link for client searches
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.UserClientsResource#getUsers(Long, org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler, String, String, Long, Long)
     */
    public static Link createFullUsersSearchLink(Client entity) {
        Link link = linkTo(methodOn(UserClientsResource.class).getUsers(entity.getId(), null, null, null, null, null, null, null)).withRel("users");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("term", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("teamId", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("tagId", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }
}
