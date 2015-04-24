package com.emmisolutions.emmimanager.web.rest.admin.model.patient;

import com.emmisolutions.emmimanager.model.Patient;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * A patient resource assembler
 */
@Component
public class AdminPatientResourceAssembler implements ResourceAssembler<Patient, AdminPatientResource> {

    @Override
    public AdminPatientResource toResource(Patient entity) {
        AdminPatientResource ret = new AdminPatientResource();
        ret.setEntity(entity);
        return ret;
    }
}
