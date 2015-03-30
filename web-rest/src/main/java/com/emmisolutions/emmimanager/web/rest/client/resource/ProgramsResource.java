package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.service.ProgramService;
import com.emmisolutions.emmimanager.service.TeamLocationService;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.web.rest.client.model.program.ProgramResource;
import com.emmisolutions.emmimanager.web.rest.client.model.program.ProgramResourcePage;
import com.emmisolutions.emmimanager.web.rest.client.model.program.location.TeamLocationResource;
import com.emmisolutions.emmimanager.web.rest.client.model.program.location.TeamLocationResourcePage;
import com.emmisolutions.emmimanager.web.rest.client.model.program.provider.TeamProviderResource;
import com.emmisolutions.emmimanager.web.rest.client.model.program.provider.TeamProviderResourcePage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Program REST Resource
 */
@RestController("clientProgramsResource")
@RequestMapping(value = "/webapi-client", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class ProgramsResource {

    @Resource
    ProgramService programService;

    @Resource
    TeamLocationService teamLocationService;

    @Resource
    TeamProviderService teamProviderService;

    @Resource(name = "programResourceAssembler")
    ResourceAssembler<Program, ProgramResource> programResourceResourceAssembler;

    @Resource(name = "clientTeamLocationResourceAssembler")
    ResourceAssembler<TeamLocation, TeamLocationResource> teamLocationResourceResourceAssembler;

    @Resource(name = "clientTeamProviderResourceAssembler")
    ResourceAssembler<TeamProvider, TeamProviderResource> teamProviderResourceResourceAssembler;

    /**
     * Find possible programs that can be scheduled for a team
     *
     * @param clientId  for which to load the programs
     * @param teamId    for which to load the programs
     * @param pageable  which page to fetch
     * @param assembler makes a page of Program objects into ProgramResource objects
     * @return a ProgramResourcePage
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/programs", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ProgramResourcePage possiblePrograms(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            PagedResourcesAssembler<Program> assembler) {

        Page<Program> programPage = programService.find(pageable);

        return new ProgramResourcePage(
                assembler.toResource(programPage, programResourceResourceAssembler),
                programPage);
    }


    /**
     * Find possible locations that can be scheduled for a team
     *
     * @param clientId  for which to load the programs
     * @param teamId    for which to load the programs
     * @param pageable  which page to fetch
     * @param assembler makes a page of domain objects into Resource objects
     * @return a TeamLocationResourcePage
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/locations", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public TeamLocationResourcePage locations(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "location.name") Pageable pageable,
            PagedResourcesAssembler<TeamLocation> assembler) {

        Page<TeamLocation> teamLocationPage =
                    teamLocationService.findAllTeamLocationsWithTeam(pageable, new Team(teamId));

        return new TeamLocationResourcePage(
                assembler.toResource(teamLocationPage, teamLocationResourceResourceAssembler),
                teamLocationPage);
    }

    /**
     * Find possible providers that can be scheduled for a team
     *
     * @param clientId  for which to load the programs
     * @param teamId    for which to load the programs
     * @param pageable  which page to fetch
     * @param assembler makes a page of domain objects into Resource objects
     * @return a TeamProviderResourcePage
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/providers", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public TeamProviderResourcePage providers(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "provider.normalizedName") Pageable pageable,
            PagedResourcesAssembler<TeamProvider> assembler) {

        Page<TeamProvider> teamProviderPage =
                    teamProviderService.findTeamProvidersByTeam(pageable, new Team(teamId));

        return new TeamProviderResourcePage(
                assembler.toResource(teamProviderPage, teamProviderResourceResourceAssembler),
                teamProviderPage);
    }
}
