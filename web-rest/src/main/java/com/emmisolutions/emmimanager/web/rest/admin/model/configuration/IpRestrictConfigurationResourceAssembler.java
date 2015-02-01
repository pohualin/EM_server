package com.emmisolutions.emmimanager.web.rest.admin.model.configuration;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.resource.IpRestrictConfigurationsResource;

/**
 * Creates a ipRestrictConfigurationResource from a ipRestrictConfiguration
 */
@Component
public class IpRestrictConfigurationResourceAssembler
        implements
        ResourceAssembler<IpRestrictConfiguration, IpRestrictConfigurationResource> {

    @Override
    public IpRestrictConfigurationResource toResource(
            IpRestrictConfiguration entity) {
        IpRestrictConfigurationResource ret = new IpRestrictConfigurationResource();
        ret.add(linkTo(
                methodOn(IpRestrictConfigurationsResource.class).get(
                        entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}
