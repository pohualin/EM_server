package com.emmisolutions.emmimanager.web.rest.admin.model.configuration;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientRestrictConfigurationsResource;

/**
 * Creates a ClientRestrictConfigurationResource from a
 * ClientRestrictConfiguration
 */
@Component
public class ClientRestrictConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientRestrictConfiguration, ClientRestrictConfigurationResource> {

    @Override
    public ClientRestrictConfigurationResource toResource(
            ClientRestrictConfiguration entity) {
        ClientRestrictConfigurationResource ret = new ClientRestrictConfigurationResource();
        ret.add(linkTo(
                methodOn(ClientRestrictConfigurationsResource.class).get(
                        entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}
