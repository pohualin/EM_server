package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.web.rest.admin.resource.PatientIdLabelConfigsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a PatientIdLabelConfigResource (which has links) from a PatientIdLabelConfig
 */
@Component
public class PatientIdLabelConfigAssembler implements
        ResourceAssembler<PatientIdLabelConfig, PatientIdLabelConfigResource> {

    @Override
    public PatientIdLabelConfigResource toResource(PatientIdLabelConfig entity) {
        PatientIdLabelConfigResource ret = new PatientIdLabelConfigResource();
        ret.add(linkTo(methodOn(PatientIdLabelConfigsResource.class).getById(entity.getPatientSelfRegConfig().getId(), entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}
