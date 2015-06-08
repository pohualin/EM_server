package com.emmisolutions.emmimanager.web.rest.client.model.team.configuration;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;

/**
 * Creates a ClientTeamEmailConfigurationResource from a
 * ClientTeamEmailConfiguration
 */
@Component
public class TeamEmailConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientTeamEmailConfiguration, TeamEmailConfigurationResource> {

   	@Override
	public TeamEmailConfigurationResource toResource(
			ClientTeamEmailConfiguration entity) {
   		TeamEmailConfigurationResource ret = new TeamEmailConfigurationResource();
  		
        ret.setEntity(entity);
        return ret;
	}
}
