package com.emmisolutions.emmimanager.web.rest.model.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.model.groups.GroupPage;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamResource;
import com.emmisolutions.emmimanager.web.rest.resource.ClientsResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamsResource;
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
        ret.add(linkTo(methodOn(TeamsResource.class).clientTeams(entity.getId(),null,null,null,null,(String [])null)).withRel("teams"));
        ret.add(LocationPage.createFullClientLocationsSearchLink(entity));
        ret.add(LocationPage.locationIdsAtClient(entity));
        ret.add(GroupPage.createFullSearchLink(entity.getId()));
        ret.add(TeamPage.createFindTeamByNormalizedNameLink(entity.getId()));
        ret.add(TeamResource.createTeamByTeamIdLink(entity.getId()));
                ret.setEntity(entity);
        return ret;
    }
}
