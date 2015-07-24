package com.emmisolutions.emmimanager.web.rest.admin.model.team;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.annotation.Resource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.web.rest.admin.model.location.LocationResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamLocationsResource;

@Component
public class TeamLocationFinderResourceAssembler implements
        ResourceAssembler<TeamLocation, TeamLocationResource> {

    @Resource
    ResourceAssembler<Team, TeamResource> teamResourceAssembler;

    @Resource
    ResourceAssembler<Location, LocationResource> locationResourceAssembler;

    @Override
    public TeamLocationResource toResource(TeamLocation entity) {
        TeamLocationResource ret = new TeamLocationResource();
        if (entity.getId() != null) {
            ret.add(linkTo(
                    methodOn(TeamLocationsResource.class).getTeamLocation(
                            entity.getTeam().getId(), entity.getId()))
                    .withSelfRel());
        }
        ret.setLocation(locationResourceAssembler.toResource(entity
                .getLocation()));
        return ret;
    }
}
