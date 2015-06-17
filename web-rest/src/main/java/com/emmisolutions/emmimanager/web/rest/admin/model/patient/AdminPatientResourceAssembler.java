package com.emmisolutions.emmimanager.web.rest.admin.model.patient;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.web.rest.admin.resource.AdminPatientsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A patient resource assembler
 */
@Component
public class AdminPatientResourceAssembler implements ResourceAssembler<Patient, AdminPatientResource> {

    @Override
    public AdminPatientResource toResource(Patient entity) {
        AdminPatientResource ret = new AdminPatientResource();
        ret.setEntity(entity);
        ret.add(linkTo(methodOn(AdminPatientsResource.class).get(entity.getId())).withSelfRel());
        ret.add(linkTo(methodOn(AdminPatientsResource.class).getReferenceData()).withRel("referenceData"));
        return ret;
    }
}
