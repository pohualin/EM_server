package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.service.PatientService;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientReferenceData;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResource;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResourcePage;
import com.emmisolutions.emmimanager.web.rest.client.model.team.configuration.TeamEmailConfigurationPage;
import com.emmisolutions.emmimanager.web.rest.client.model.team.configuration.TeamEmailConfigurationResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    ClientService clientService;

    @Resource
    PatientResourceAssembler patientResourceAssembler;
    
    @Resource
    ClientTeamEmailConfigurationService clientTeamEmailConfigurationService;
    
    @Resource
    TeamEmailConfigurationResourceAssembler emailConfigurationAssembler;

    /**
     * POST for creating a patient for a given client
     *
     * @param clientId the client id
     * @param patient  to create
     * @return OK (200): when created
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patient", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<PatientResource> create(@PathVariable("clientId") Long clientId, @PathVariable("teamId") Long teamId, @RequestBody Patient patient) {
        patient.setClient(clientService.reload(new Client(clientId)));
        return new ResponseEntity<>(patientResourceAssembler.toResource(patientService.create(patient)), HttpStatus.OK);
    }

    /**
     * GET to Retrieve reference data for patients.
     *
     * @return PatientReferenceData
     */
    @RequestMapping(value = "/patients/ref", method = RequestMethod.GET)
    public PatientReferenceData getReferenceData() {
        return new PatientReferenceData();
    }

    /**
     * GET for a patient with the passed in ID
     *
     * @param clientId for security, ensures logged in user has rights to the client
     * @param patientId to retrieve
     * @return OK (200): containing  PatientResource
     *
     * GONE (410): when there isn't a patent
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patient", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<PatientResource> get(@PathVariable("clientId") Long clientId,
                                               @PathVariable("teamId") Long teamId,
                                               @RequestParam(value = "patientId", required = false) Long patientId) {
        if (patientId != null) {
            Patient toLoad = new Patient();
            toLoad.setId(patientId);
            return new ResponseEntity<>(patientResourceAssembler.toResource(patientService.reload(toLoad)), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    /**
     * GET for searching for patients
     *
     * @param clientId  for security, ensures logged in user has rights to search the client
     * @param page  the page specification
     * @param assembler to create PatientResource objects
     * @param name of the patient to search for
     * @return OK (200): containing a PatientResourcePage
     *
     * NO_CONTENT (204): when there are no matches
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patients", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<PatientResourcePage> list(@PathVariable("clientId") Long clientId,
                                                    @PageableDefault(size = 10, sort = "lastName") Pageable page,
                                                    PagedResourcesAssembler<Patient> assembler,
                                                    @RequestParam(value = "name", required = false) String name,
                                                    @PathVariable("teamId") Long teamId) {

        PatientSearchFilter filter = new PatientSearchFilter(new Client(clientId), name);
        Page<Patient> patientsPage = patientService.list(page, filter);
        if (patientsPage.hasContent()) {
            return new ResponseEntity<>(
                    new PatientResourcePage(assembler
                            .toResource(patientsPage, patientResourceAssembler), patientsPage, filter),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * PUT for updating a patient
     *
     * @param patient to update
     * @param clientId for security, ensures logged in user has rights to search the client
     * @return OK (200): containing PatientResource
     * INTERNAL_SERVER_ERROR (500): when the update doesn't return an updated patient.
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patient", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<PatientResource> update(@RequestBody Patient patient,  @PathVariable("clientId") Long clientId, @PathVariable("teamId") Long teamId){
        Patient updatedPatient = patientService.update(patient);
        if (updatedPatient == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(patientResourceAssembler.toResource(updatedPatient), HttpStatus.OK);
        }
    }

    /**
     * GET for searching for patients
     *
     * @param clientId  for security, ensures logged in user has rights to search the client
     * @param page  the page specification
     * @param assembler to create PatientResource objects
     * @return OK (200): containing a PatientResourcePage
     *
     * NO_CONTENT (204): when there are no matches
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/clientPatients", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<PatientResourcePage> listAllPatients(@PathVariable("clientId") Long clientId,
                                                    @PageableDefault(size = 10, sort = "lastName") Pageable page,
                                                    PagedResourcesAssembler<Patient> assembler,
//                                                    @RequestParam(value = "name", required = false) String name,
                                                    @PathVariable("teamId") Long teamId) {

        PatientSearchFilter filter = new PatientSearchFilter(new Client(clientId));
        Page<Patient> patientsPage = patientService.list(page, filter);
        if (patientsPage.hasContent()) {
            return new ResponseEntity<>(
                    new PatientResourcePage(assembler
                            .toResource(patientsPage, patientResourceAssembler), patientsPage, filter),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Find team email configuration for patient's or return the default
     *
     * @param teamId    for the email configuration
     * @param pageable  which page to fetch
     * @param assembler makes a page for ClientTeamEmailConfiguration
     * @return a ClientTeamEmailConfiguration response entity
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patient_email_configuration", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<TeamEmailConfigurationPage> findTeamEmailConfigForPatient(
    		@PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "rank") Pageable pageable,
            PagedResourcesAssembler<ClientTeamEmailConfiguration> assembler) {
    	
        Page<ClientTeamEmailConfiguration> page = clientTeamEmailConfigurationService
                .findByTeam(new Team(teamId), pageable);
    	if (page.hasContent()) {
    	 return new ResponseEntity<>(new TeamEmailConfigurationPage(
                 assembler.toResource(page,
                		 emailConfigurationAssembler), page),
                 HttpStatus.OK);
    	}else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        
    }
}
