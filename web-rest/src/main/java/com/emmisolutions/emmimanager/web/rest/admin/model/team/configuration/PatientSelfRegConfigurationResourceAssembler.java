package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg.InfoHeaderConfigPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg.PatientIdLabelConfigPage;
import com.emmisolutions.emmimanager.web.rest.admin.resource.PatientSelfRegConfigurationsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a PatientSelfRegConfigurationResource from a
 * PatientSelfRegConfig
 */
@Component
public class PatientSelfRegConfigurationResourceAssembler
        implements
        ResourceAssembler<PatientSelfRegConfig, PatientSelfRegConfigurationResource> {

    @Override
    public PatientSelfRegConfigurationResource toResource(
            PatientSelfRegConfig entity) {
        PatientSelfRegConfigurationResource ret = new PatientSelfRegConfigurationResource();
        ret.add(linkTo(methodOn(PatientSelfRegConfigurationsResource.class).getById(entity.getId())).withSelfRel());
        ret.add(InfoHeaderConfigPage.createFullSearchLink(entity));
        ret.add(PatientIdLabelConfigPage.createFullSearchLink(entity));
        ret.setEntity(entity);
        return ret;
    }
}
