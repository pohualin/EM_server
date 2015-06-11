package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.PatientService;
import com.emmisolutions.emmimanager.web.rest.admin.model.patient.AdminPatientResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.patient.AdminPatientResourcePage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.List;

import static com.emmisolutions.emmimanager.model.PatientSearchFilter.with;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Patients REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class AdminPatientsResource {

    /**
     * request parameter name
     */
    public static final String ACCESS_CODES_REQUEST_PARAM = "accessCode";
    /**
     * request parameter name
     */
    public static final String EMAILS_REQUEST_PARAM = "email";
    /**
     * request parameter name
     */
    public static final String PHONES_REQUEST_PARAM = "phone";
    /**
     * request parameter name
     */
    public static final String NAME_REQUEST_PARAM = "name";


    @Resource
    PatientService patientService;

    @Resource
    ClientService clientService;

    @Resource(name = "adminPatientResourceAssembler")
    ResourceAssembler<Patient, AdminPatientResource> adminPatientResourceAssembler;

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

    /**
     * GET to find patients
     *
     * @return OK (200): containing a AdminPatientResourcePage
     */
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = NAME_REQUEST_PARAM, defaultValue = "", value = "name filter", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = ACCESS_CODES_REQUEST_PARAM, defaultValue = "", value = "access code filter", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = PHONES_REQUEST_PARAM, defaultValue = "", value = "phone number filter", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = EMAILS_REQUEST_PARAM, defaultValue = "", value = "email filter", dataType = "string", paramType = "query")
    })
    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<AdminPatientResourcePage> find(@PageableDefault(size = 10, sort = "lastName") Pageable page,
                                                         PagedResourcesAssembler<Patient> assembler,
                                                         @RequestParam(value = NAME_REQUEST_PARAM, required = false) String name,
                                                         @RequestParam(value = ACCESS_CODES_REQUEST_PARAM, required = false)
                                                         List<String> accessCodes,
                                                         @RequestParam(value = PHONES_REQUEST_PARAM, required = false)
                                                         List<String> phones,
                                                         @RequestParam(value = EMAILS_REQUEST_PARAM, required = false)
                                                         List<String> emails) {
        PatientSearchFilter filter = with().lastScheduledProgramLoaded()
                .names(name).phones(phones).accessCodes(accessCodes).emails(emails);
        Page<Patient> patientsPage = patientService.list(page, filter);
        if (patientsPage.hasContent()) {
            return new ResponseEntity<>(
                    new AdminPatientResourcePage(assembler
                            .toResource(patientsPage, adminPatientResourceAssembler),
                            patientsPage, filter),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
