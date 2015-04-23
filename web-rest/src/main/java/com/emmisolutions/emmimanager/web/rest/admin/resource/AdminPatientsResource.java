package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.PatientService;
import com.emmisolutions.emmimanager.web.rest.admin.model.patient.AdminPatientResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.patient.AdminPatientResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Patients REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class AdminPatientsResource {

    @Resource
    PatientService patientService;

    @Resource
    ClientService clientService;

    @Resource
    AdminPatientResourceAssembler adminPatientResourceAssembler;

    /**
     * POST for creating a patient for a given client
     *
     * @param clientId the client id
     * @param patient  to create
     * @return OK (200): when created
     */
    @RequestMapping(value = "/clients/{clientId}/patient", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<AdminPatientResource> create(@PathVariable("clientId") Long clientId, @RequestBody Patient patient) {
        patient.setClient(clientService.reload(new Client(clientId)));
        return new ResponseEntity<>(adminPatientResourceAssembler.toResource(patientService.create(patient)), HttpStatus.OK);
    }
}
