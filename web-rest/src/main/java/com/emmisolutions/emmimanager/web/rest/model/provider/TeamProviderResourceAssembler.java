package com.emmisolutions.emmimanager.web.rest.model.provider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.resource.ProvidersResource;

@Component
public class TeamProviderResourceAssembler implements ResourceAssembler<TeamProvider, TeamProviderResource> {

//	@Resource
//	ProviderResourceAssembler providerResourceAssembler;
	
	@Override
	public TeamProviderResource toResource(TeamProvider entity){
		TeamProviderResource ret = new TeamProviderResource();
		
//        ret.add(linkTo(methodOn(ProvidersResource.class).getById(entity.getId(), entity.getTeam().getId(), entity.getTeam().getClient().getId())).withSelfRel());
		ret.setEntity(entity);
		return ret;
	}
}
