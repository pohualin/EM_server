package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.web.rest.client.model.schedule.ScheduledProgramResource;
import com.emmisolutions.emmimanager.web.rest.client.model.schedule.ScheduledProgramResourcePage;
import com.emmisolutions.emmimanager.web.rest.client.model.team.TeamResource;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Teams resource.
 */
@RestController("clientSchedulesResource")
@RequestMapping(value = "/webapi-client", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class SchedulesResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesResource.class);

    @Resource(name = "scheduledProgramResourceAssembler")
    ResourceAssembler<ScheduledProgram, ScheduledProgramResource> scheduledProgramResourceResourceAssembler;

    @Resource(name = "clientTeamResourceAssembler")
    ResourceAssembler<Team, TeamResource> teamTeamResourceAssembler;

    @Resource
    TeamService teamService;

    @Resource
    ScheduleService scheduleService;

    /**
     * GET to retrieve a page of ScheduledProgram objects for a particular team.
     *
     * @return ScheduledProgramResourcePage when authorized or 403 if the user is not
     * authorized.
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/schedules", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ScheduledProgramResourcePage scheduled(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<ScheduledProgram> assembler) {

        LOGGER.debug("Client ID: {}", clientId);
        LOGGER.debug("Team ID: {}", teamId);
        LOGGER.debug("Pageable: {}", pageable);

        Page<ScheduledProgram> scheduledPrograms = new PageImpl<>(new ArrayList<ScheduledProgram>());

        return new ScheduledProgramResourcePage(
                assembler.toResource(scheduledPrograms, scheduledProgramResourceResourceAssembler),
                scheduledPrograms);
    }

    /**
     * Fetches a particular schedule for a team
     *
     * @param clientId for this client
     * @param teamId for this team
     * @param scheduleId the schedule id
     * @return 200 (OK): ScheduledProgramResource
     * 204 (NO_CONTENT): if not found
     * 403 (FORBIDDEN): if the user doesn't have rights to the client and/or team
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/schedules/{scheduleId}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<ScheduledProgramResource> aScheduled(@PathVariable("clientId") Long clientId,
                                                              @PathVariable("teamId") Long teamId,
                                                              @PathVariable("scheduleId") Long scheduleId) {
        ScheduledProgram scheduledProgram = scheduleService.reload(new ScheduledProgram(scheduleId, new Team(teamId)));
        if (scheduledProgram != null) {
            return new ResponseEntity<>(scheduledProgramResourceResourceAssembler.toResource(scheduledProgram), OK);
        }
        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * POST to schedule a program.
     *
     * @return 200 (OK): a ScheduledProgramResource for the newly scheduled program
     * 500 (INTERNAL_SERVER_ERROR): if there were a problem scheduling the program
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/schedules", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<ScheduledProgramResource> schedule(@PathVariable("clientId") Long clientId,
                                                             @PathVariable("teamId") Long teamId,
                                                             @RequestBody ScheduledProgram toBeScheduled) {

        // caller has permission to schedule programs for the client and team
    	Team securedTeam = new Team(teamId);
        securedTeam.setClient(new Client(clientId));
        toBeScheduled.setTeam(securedTeam);
       
        // schedule program
        ScheduledProgram scheduledProgram = scheduleService.schedule(toBeScheduled);
        if (scheduledProgram != null) {
            return new ResponseEntity<>(scheduledProgramResourceResourceAssembler.toResource(scheduledProgram), OK);
        }

        return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }

    /**
     * Load a team with links that have to do with scheduling
     *
     * @param clientId to ensure permissions
     * @param teamId   to ensure permissions
     * @return 200 (OK): a TeamResource if the user has permission to load the user,
     * 403 (DENIED): if the user is not authorized for the client and team or if the team or client are invalid
     * 410 (GONE): if the user has access to the team and client but the team doesn't belong to that client
     */
    @RequestMapping(value = "/clients/{clientId}/teams/", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<TeamResource> loadTeamForScheduling(
            @PathVariable("clientId") Long clientId,
            @RequestParam(value = "teamId", required = false) Long teamId) {

        if (teamId != null) {
            Team found = teamService.reload(new Team(teamId));
            if (found != null && found.getClient().getId().equals(clientId)) {
                return new ResponseEntity<>(teamTeamResourceAssembler.toResource(found), OK);
            }
        }
        return new ResponseEntity<>(GONE);
    }

    /**
     * GET to retrieve a page of ScheduledProgram objects for a particular patient.
     *
     * @return ScheduledProgramResourcePage when authorized or 403 if the user is not
     * authorized.
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/patient_schedule", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "createdDate,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ScheduledProgramResourcePage getPatientSchedules(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.ASC) Pageable page,

        PagedResourcesAssembler<ScheduledProgram> assembler) {

        Page<ScheduledProgram> scheduledPrograms = scheduleService.findAllByPatient(new Patient(patientId), page);

        return new ScheduledProgramResourcePage(
                assembler.toResource(scheduledPrograms, scheduledProgramResourceResourceAssembler),
                scheduledPrograms);
    }
}
