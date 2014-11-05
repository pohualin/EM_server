package com.emmisolutions.emmimanager.web.rest.model.team;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.resource.TeamProvidersResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamTagsResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
	     ret.add(linkTo(methodOn(TeamsResource.class).getTeam(entity.getClient().getId(), entity.getId())).withSelfRel());
         ret.add(linkTo(methodOn(TeamTagsResource.class).list(1l, null, null, null, null, null)).withRel("tags"));
         ret.add(ProviderPage.createProviderReferenceDataLink().withRel("providerReferenceData"));
         ret.add(ProviderPage.createProviderLink(entity.getClient().getId(), entity.getId()).withRel("provider"));
         ret.add(linkTo(methodOn(TeamTagsResource.class).list(entity.getId(), null, null, null, null, null)).withRel("tags"));
         ret.add(TeamLocationPage.createFullSearchLink(entity));
         ret.add(linkTo(methodOn(TeamProvidersResource.class).list(entity.getId(), null, null, null, null, null)).withRel("teamProviders"));
         ret.setEntity(entity);
	     return ret;
	 }
}
