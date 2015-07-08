package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

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

        ret.setEntity(entity);
        return ret;
    }
}
