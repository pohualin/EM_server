package com.emmisolutions.emmimanager.web.rest.client.model.patient;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

/**
 * A page of Patient Resource objects
 */
public class PatientResourcePage extends PagedResource<PatientResource> {

    public PatientResourcePage() {
    }

    /**
     * contructor for Patient resource page
     * @param pagedPatientResources
     * @param patientPage
     */
    public PatientResourcePage(PagedResources<PatientResource> pagedPatientResources, Page<Patient> patientPage) {
        pageDefaults(pagedPatientResources, patientPage);
    }
}
