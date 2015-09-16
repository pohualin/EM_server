package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientContentSubscriptionConfigurationsResource;

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
   	    ret.add(linkTo(methodOn(ClientContentSubscriptionConfigurationsResource.class).get(entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
	}
}
