package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.service.PatientService;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResource;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Patients REST API
 */
@RestController
@RequestMapping(value = "/webapi-client", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class PatientsResource {

    @Resource
    PatientService patientService;

    @Resource
    PatientResourceAssembler assembler;

    @RequestMapping(value = "/{clientId}/create", method = RequestMethod.POST)
    public ResponseEntity<PatientResource> create(@PathVariable("clientId") Long clientId, @RequestBody Patient patient) {

        return new ResponseEntity<PatientResource>(assembler.toResource(patientService.create(patient)), HttpStatus.OK);
    }
}
