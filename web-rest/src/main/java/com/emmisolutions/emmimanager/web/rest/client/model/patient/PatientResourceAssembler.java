package com.emmisolutions.emmimanager.web.rest.client.model.patient;

import com.emmisolutions.emmimanager.model.Patient;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

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
