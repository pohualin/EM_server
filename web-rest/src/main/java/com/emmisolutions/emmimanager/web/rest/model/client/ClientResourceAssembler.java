package com.emmisolutions.emmimanager.web.rest.model.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.model.clientlocation.ClientLocationResourcePage;
import com.emmisolutions.emmimanager.web.rest.model.clientprovider.ClientProviderResourcePage;
import com.emmisolutions.emmimanager.web.rest.model.groups.GroupPage;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamResource;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientRoleResourcePage;
import com.emmisolutions.emmimanager.web.rest.model.user.client.team.UserClientTeamRoleResourcePage;
import com.emmisolutions.emmimanager.web.rest.resource.*;
import org.springframework.hateoas.ResourceAssembler;
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
        ret.add(linkTo(methodOn(TeamsResource.class).clientTeams(entity.getId(), null, null, null, null, (String[]) null)).withRel("teams"));
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
        ret.add(linkTo(methodOn(UsersClientResource.class).getUsers(entity.getId(), null, null, null, null, null)).withRel("users"));
        ret.setEntity(entity);
        return ret;
    }
}
