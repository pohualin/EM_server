package com.emmisolutions.emmimanager.web.rest.admin.model.configuration;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.resource.EmailRestrictConfigurationsResource;

/**
 * Creates a EmailRestrictConfigurationResource from a
 * EmailRestrictConfiguration
 */
@Component
public class EmailRestrictConfigurationResourceAssembler
        implements
        ResourceAssembler<EmailRestrictConfiguration, EmailRestrictConfigurationResource> {

    @Override
    public EmailRestrictConfigurationResource toResource(
            EmailRestrictConfiguration entity) {
        EmailRestrictConfigurationResource ret = new EmailRestrictConfigurationResource();
        ret.add(linkTo(
                methodOn(EmailRestrictConfigurationsResource.class).get(
                        entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}
