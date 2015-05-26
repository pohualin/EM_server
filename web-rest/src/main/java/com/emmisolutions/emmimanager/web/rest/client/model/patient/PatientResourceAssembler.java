package com.emmisolutions.emmimanager.web.rest.client.model.patient;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.web.rest.client.resource.PatientsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A patient resource assembler
 */
@Component
public class PatientResourceAssembler implements ResourceAssembler<Patient, PatientResource> {

    @Override
    public PatientResource toResource(Patient entity) {
        PatientResource ret = new PatientResource();
        ret.setEntity(entity);
        return ret;
    }
}
