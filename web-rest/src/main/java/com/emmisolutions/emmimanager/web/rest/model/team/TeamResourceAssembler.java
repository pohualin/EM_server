package com.emmisolutions.emmimanager.web.rest.model.team;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.emmisolutions.emmimanager.web.rest.resource.TeamTagsResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.resource.TeamsResource;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

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
	     ret.add(linkTo(methodOn(TeamsResource.class).getTeam(entity.getId())).withSelfRel());
         ret.add(linkTo(methodOn(TeamTagsResource.class).list(1l,null,null,null,null,null)).withSelfRel());
	     ret.setEntity(entity);
	     return ret; 
	 }
}
