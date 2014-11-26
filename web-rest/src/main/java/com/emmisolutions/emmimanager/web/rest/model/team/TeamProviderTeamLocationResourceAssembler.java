package com.emmisolutions.emmimanager.web.rest.model.team;

import javax.annotation.Resource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.web.rest.model.provider.TeamProviderResourceAssembler;

/**
 * Responsible for creating a TeamLocationResource (which has links) from a TeamLocation
 */
@Component
public class TeamProviderTeamLocationResourceAssembler implements ResourceAssembler<TeamProviderTeamLocation, TeamProviderTeamLocationResource> {

	@Resource
	TeamLocationResourceAssembler teamLocationResourceAssembler;
	
	@Resource
	TeamProviderResourceAssembler teamProviderResourceAssembler;
	
    @Override
    public TeamProviderTeamLocationResource toResource(TeamProviderTeamLocation entity) {
    	TeamProviderTeamLocationResource ret = new TeamProviderTeamLocationResource();
    	ret.setTeamProviderTeamLocationId(entity.getId());
    	ret.setTeamLocation(teamLocationResourceAssembler.toResource(entity.getTeamLocation()));
    	ret.setTeamProvider(teamProviderResourceAssembler.toResource(entity.getTeamProvider()));
        return ret;
    }
}