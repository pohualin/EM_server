package com.emmisolutions.emmimanager.web.rest.client.model.team.configuration;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;

/**
 * Creates a ClientTeamPhoneConfigurationResource from a
 * ClientTeamPhoneConfiguration
 */
@Component
public class TeamPhoneConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientTeamPhoneConfiguration, TeamPhoneConfigurationResource> {

   	@Override
	public TeamPhoneConfigurationResource toResource(
			ClientTeamPhoneConfiguration entity) {
   		TeamPhoneConfigurationResource ret = new TeamPhoneConfigurationResource();
  		
        ret.setEntity(entity);
        return ret;
	}
}
