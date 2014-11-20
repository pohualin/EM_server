package com.emmisolutions.emmimanager.web.rest.model.provider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.resource.TeamProvidersResource;

@Component
public class TeamProviderResourceAssembler implements ResourceAssembler<TeamProvider, TeamProviderResource> {

	@Override
	public TeamProviderResource toResource(TeamProvider entity) {
		TeamProviderResource ret = new TeamProviderResource();
		ret.add(TeamProviderPage.createProviderByIdLink(entity.getId()));
        ret.add(linkTo(methodOn(TeamProvidersResource.class).getById(entity.getId())).withSelfRel());
		ret.setEntity(entity);
		ret.setProvider(new ProviderResource(entity.getProvider()));
		return ret;
	}
}
