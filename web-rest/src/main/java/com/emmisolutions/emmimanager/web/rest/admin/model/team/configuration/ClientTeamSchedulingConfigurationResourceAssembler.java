package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;

/**
 * Creates a ClientTeamSchedulingConfigurationResource from a
 * ClientTeamSchedulingConfiguration
 */
@Component
public class ClientTeamSchedulingConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientTeamSchedulingConfiguration, ClientTeamSchedulingConfigurationResource> {

   	@Override
	public ClientTeamSchedulingConfigurationResource toResource(
			ClientTeamSchedulingConfiguration entity) {
   		ClientTeamSchedulingConfigurationResource ret = new ClientTeamSchedulingConfigurationResource();
  		
        ret.setEntity(entity);
        return ret;
	}
}
