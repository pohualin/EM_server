package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;

/**
 * Creates a ClientContentSubscriptionConfigurationResource from a
 * ClientContentSubscriptionConfiguration
 */
@Component
public class ClientContentSubscriptionConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientContentSubscriptionConfiguration, ClientContentSubscriptionConfigurationResource> {

   	@Override
	public ClientContentSubscriptionConfigurationResource toResource(
			ClientContentSubscriptionConfiguration entity) {
   		ClientContentSubscriptionConfigurationResource ret = new ClientContentSubscriptionConfigurationResource();
  		
        ret.setEntity(entity);
        return ret;
	}
}
