package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;

/**
 * Creates a ClientTeamPhoneConfigurationResource from a
 * ClientTeamPhoneConfiguration
 */
@Component
public class ClientTeamPhoneConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientTeamPhoneConfiguration, ClientTeamPhoneConfigurationResource> {

   	@Override
	public ClientTeamPhoneConfigurationResource toResource(
			ClientTeamPhoneConfiguration entity) {
   		ClientTeamPhoneConfigurationResource ret = new ClientTeamPhoneConfigurationResource();
  		
        ret.setEntity(entity);
        return ret;
	}
}
