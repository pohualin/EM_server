package com.emmisolutions.emmimanager.web.rest.model.team;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.resource.TeamsResource;

/**
 * 
 * Responsible for creating a TeamResource (which has links) from a Team
 *
 */
@Component
public class TeamResourceAssembler implements ResourceAssembler<Team, TeamResource> {
	
	 @Override
	 public TeamResource toResource(Team entity) {
		 TeamResource ret = new TeamResource();
	     ret.add(linkTo(methodOn(TeamsResource.class).get(entity.getId())).withSelfRel());
	     ret.setEntity(entity);
	     return ret; 
	 }
}
