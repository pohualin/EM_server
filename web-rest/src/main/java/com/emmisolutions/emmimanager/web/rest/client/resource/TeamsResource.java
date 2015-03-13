package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.web.rest.client.model.schedule.ScheduledProgramResource;
import com.emmisolutions.emmimanager.web.rest.client.model.schedule.ScheduledProgramResourcePage;
import com.emmisolutions.emmimanager.web.rest.client.model.team.TeamResource;
import com.emmisolutions.emmimanager.web.rest.client.model.team.UserClientUserClientTeamRoleTeamResourceAssembler;
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
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Teams resource.
 */
@RestController("clientTeamsResource")
@RequestMapping(value = "/webapi-client", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class TeamsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamsResource.class);

    @Resource(name = "scheduledProgramResourceAssembler")
    ResourceAssembler<ScheduledProgram, ScheduledProgramResource> programResourceResourceAssembler;

    @Resource(name = "userClientUserClientTeamRoleTeamResourceAssembler")
    UserClientUserClientTeamRoleTeamResourceAssembler roleTeamResourceResourceAssembler;

    @Resource
    TeamService teamService;

    /**
     * GET to retrieve a page of ScheduledProgram objects for a particular team.
     *
     * @return ScheduledProgramResourcePage when authorized or 403 if the user is not
     * authorized.
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/schedulePrograms", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ScheduledProgramResourcePage schedule(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @SortDefault(sort = "id") Sort sort,
            PagedResourcesAssembler<ScheduledProgram> assembler) {

        LOGGER.debug("Client ID: {}", clientId);
        LOGGER.debug("Team ID: {}", teamId);
        LOGGER.debug("Pageable: {}", pageable);
        LOGGER.debug("Sort: {}", sort);

        Page<ScheduledProgram> scheduledPrograms = new PageImpl<>(new ArrayList<ScheduledProgram>());

        return new ScheduledProgramResourcePage(
                assembler.toResource(scheduledPrograms, programResourceResourceAssembler),
                scheduledPrograms);
    }

    /**
     * Load a team with links that have to do with scheduling
     *
     * @param clientId to ensure permissions
     * @param teamId   to ensure permissions
     * @return 200 (OK): a TeamResource if the user has permission to load the user,
     * 403 (DENIED): if the user is not authorized for the client and team or if the team or client are invalid
     * 410 (GONE: if the user has access to the team and client but the team doesn't belong to that client
     */
    @RequestMapping(value = "/clients/{clientId}/teams/", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    public ResponseEntity<TeamResource> loadTeamForScheduling(
            @PathVariable("clientId") Long clientId,
            @RequestParam(value = "teamId", required = false) Long teamId) {

        if (teamId != null) {
            Team found = teamService.reload(new Team(teamId));
            if (found != null && found.getClient().getId().equals(clientId)) {
                TeamResource ret = new TeamResource();
                ret.setEntity(found);
                ret.add(roleTeamResourceResourceAssembler.createScheduleLink(found));
                return new ResponseEntity<>(ret, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }
}
