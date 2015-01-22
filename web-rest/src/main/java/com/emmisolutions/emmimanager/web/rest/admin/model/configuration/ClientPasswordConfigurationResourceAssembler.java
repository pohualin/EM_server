package com.emmisolutions.emmimanager.web.rest.admin.model.configuration;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientPasswordConfigurationsResource;

/**
 * Creates a ClientPasswordConfigurationResource from a
 * ClientPasswordConfiguration
 */
@Component
public class ClientPasswordConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientPasswordConfiguration, ClientPasswordConfigurationResource> {

    /**
     * Compose ClientPasswordConfigurationResource to return
     */
    @Override
    public ClientPasswordConfigurationResource toResource(
            ClientPasswordConfiguration entity) {
        ClientPasswordConfigurationResource ret = new ClientPasswordConfigurationResource();
        if (entity.getId() != null) {
            ret.add(linkTo(
                    methodOn(ClientPasswordConfigurationsResource.class).get(
                            entity.getId())).withSelfRel());
        }
        ret.setEntity(entity);
        return ret;
    }
}
