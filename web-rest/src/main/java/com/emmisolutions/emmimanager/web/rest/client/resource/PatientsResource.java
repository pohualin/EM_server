package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.service.ClientTeamPhoneConfigurationService;
import com.emmisolutions.emmimanager.service.PatientService;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientReferenceData;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResource;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResourcePage;
import com.emmisolutions.emmimanager.web.rest.client.model.team.configuration.TeamEmailConfigurationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.team.configuration.TeamPhoneConfigurationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.team.configuration.TeamEmailConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.client.model.team.configuration.TeamPhoneConfigurationResource;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.PatientSearchFilter.with;
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
    ClientTeamPhoneConfigurationService clientTeamPhoneConfigurationService;
    
    @Resource
    ClientTeamEmailConfigurationService clientTeamEmailConfigurationService;

    @Resource
    TeamEmailConfigurationResourceAssembler emailConfigurationAssembler;
    
    @Resource
    TeamPhoneConfigurationResourceAssembler phoneConfigurationAssembler;

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
     * @param clientId  for security, ensures logged in user has rights to the client
     * @param patientId to retrieve
     * @return OK (200): containing  PatientResource
     * <p/>
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
     * @param page      the page specification
     * @param assembler to create PatientResource objects
     * @param name      of the patient to search for
     * @return OK (200): containing a PatientResourcePage
     * <p/>
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
        PatientSearchFilter with = with().client(new Client(clientId)).names(name);
        Page<Patient> patientsPage = patientService.list(page, with);
        if (patientsPage.hasContent()) {
            return new ResponseEntity<>(
                    new PatientResourcePage(assembler
                            .toResource(patientsPage, patientResourceAssembler), patientsPage, with),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * PUT for updating a patient
     *
     * @param patient  to update
     * @param clientId for security, ensures logged in user has rights to search the client
     * @return OK (200): containing PatientResource
     * INTERNAL_SERVER_ERROR (500): when the update doesn't return an updated patient.
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patient", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<PatientResource> update(@RequestBody Patient patient, @PathVariable("clientId") Long clientId, @PathVariable("teamId") Long teamId) {
        Patient updatedPatient = patientService.update(patient);
        if (updatedPatient == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(patientResourceAssembler.toResource(updatedPatient), HttpStatus.OK);
        }
    }

    /**
     * Find a team email configuration for a particular patient or return the default
     * @param clientId a client's ID
     * @param teamId a team's ID
     * @return a ClientTeamEmailConfiguration
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patient_email_configuration", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<TeamEmailConfigurationResource> findTeamEmailConfigurationForPatient(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId) {
        ClientTeamEmailConfiguration clientTeamEmailConfiguration = clientTeamEmailConfigurationService.findByTeam(new Team(teamId));

        if (clientTeamEmailConfiguration != null) {
            return new ResponseEntity<>(emailConfigurationAssembler.toResource(clientTeamEmailConfiguration), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    /**
     * Find team phone configuration for patient's or return the default
     *
     * @param clientId    for the phone configuration
     * @param teamId    for the phone configuration
     * @return a ClientTeamPhoneConfiguration response entity
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patient_phone_configuration", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<TeamPhoneConfigurationResource> findTeamPhoneConfigForPatient(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId) {

        ClientTeamPhoneConfiguration teamPhoneConfiguration = clientTeamPhoneConfigurationService.findByTeam(new Team(teamId));

        if (teamPhoneConfiguration != null) {
            return new ResponseEntity<>(phoneConfigurationAssembler.toResource(teamPhoneConfiguration), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    /**
     * GET for listing all scheduled patients the given team
     *
     * @param clientId  for security, ensures logged in user has rights to search the client
     * @param page      the page specification
     * @param assembler to create PatientResource objects
     * @param teamId    team for which to see all patients scheduled
     * @return OK (200): containing a PatientResourcePage
     * <p/>
     * NO_CONTENT (204): when there are no matches
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patientsScheduled", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "createdDate,desc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<PatientResourcePage> listAllPatientsScheduledForTeam(@PathVariable("clientId") Long clientId,
                                                    @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable page,
                                                    PagedResourcesAssembler<Patient> assembler,
                                                    @PathVariable("teamId") Long teamId) {
        PatientSearchFilter with = with().teams(new Team(teamId));
        Page<Patient> patientsPage = patientService.list(page, with);
        if (patientsPage.hasContent()) {
            return new ResponseEntity<>(
                    new PatientResourcePage(assembler
                            .toResource(patientsPage, patientResourceAssembler), patientsPage, with),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
