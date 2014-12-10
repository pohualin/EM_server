package com.emmisolutions.emmimanager.web.rest.model.team;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.web.rest.resource.TeamLocationsResource;

/**
 * Responsible for creating a TeamLocationResource (which has links) from a TeamLocation
 */
@Component
public class TeamLocationResourceAssembler implements ResourceAssembler<TeamLocation, TeamLocationResource> {

    @Override
    public TeamLocationResource toResource(TeamLocation entity) {
    	TeamLocationResource ret = new TeamLocationResource();
        ret.add(linkTo(methodOn(TeamLocationsResource.class).getTeamLocation(entity.getTeam().getId(), entity.getId())).withSelfRel());
        ret.add(linkTo(methodOn(TeamLocationsResource.class).listTeamProviderTeamLocation(entity.getId(), null, null)).withRel("tptls"));
        ret.setEntity(entity);
        return ret;
    }
}