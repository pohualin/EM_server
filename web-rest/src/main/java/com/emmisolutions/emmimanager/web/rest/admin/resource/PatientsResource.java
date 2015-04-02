package com.emmisolutions.emmimanager.web.rest.admin.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
/**
 * Patients REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class PatientsResource {


}
