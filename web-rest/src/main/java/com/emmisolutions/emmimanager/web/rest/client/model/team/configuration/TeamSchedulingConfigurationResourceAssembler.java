package com.emmisolutions.emmimanager.web.rest.client.model.team.configuration;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;

/**
 * Creates a TeamSchedulingConfigurationResource from a
 * TeamSchedulingConfiguration
 */
@Component
public class TeamSchedulingConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientTeamSchedulingConfiguration, TeamSchedulingConfigurationResource> {

   	@Override
	public TeamSchedulingConfigurationResource toResource(
			ClientTeamSchedulingConfiguration entity) {
   		TeamSchedulingConfigurationResource ret = new TeamSchedulingConfigurationResource();
  		
        ret.setEntity(entity);
        return ret;
	}
}
