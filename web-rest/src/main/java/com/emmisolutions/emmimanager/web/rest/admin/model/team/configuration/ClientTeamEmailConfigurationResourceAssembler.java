package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientTeamEmailConfigurationsResource;

/**
 * Creates a ClientTeamEmailConfigurationResource from a
 * ClientTeamEmailConfiguration
 */
@Component
public class ClientTeamEmailConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientTeamEmailConfiguration, ClientTeamEmailConfigurationResource> {

   	@Override
	public ClientTeamEmailConfigurationResource toResource(
			ClientTeamEmailConfiguration entity) {
   		ClientTeamEmailConfigurationResource ret = new ClientTeamEmailConfigurationResource();
  		
        ret.setEntity(entity);
        return ret;
	}
}
