package com.emmisolutions.emmimanager.web.rest.model.provider;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.TeamProvider;

@Component
public class TeamProviderResourceAssembler implements ResourceAssembler<TeamProvider, TeamProviderResource> {

	@Override
	public TeamProviderResource toResource(TeamProvider entity) {
		TeamProviderResource ret = new TeamProviderResource();
		ret.add(TeamProviderPage.createProviderByIdLink(entity.getId()));
		ret.setEntity(entity);
		return ret;
	}
}
