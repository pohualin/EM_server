package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.service.ProgramService;
import com.emmisolutions.emmimanager.service.TeamLocationService;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.client.model.program.ProgramResource;
import com.emmisolutions.emmimanager.web.rest.client.model.program.ProgramResourcePage;
import com.emmisolutions.emmimanager.web.rest.client.model.program.location.TeamLocationResource;
import com.emmisolutions.emmimanager.web.rest.client.model.program.location.TeamLocationResourcePage;
import com.emmisolutions.emmimanager.web.rest.client.model.program.provider.TeamProviderResource;
import com.emmisolutions.emmimanager.web.rest.client.model.program.provider.TeamProviderResourcePage;
import com.emmisolutions.emmimanager.web.rest.client.model.program.specialty.SpecialtyResource;
import com.emmisolutions.emmimanager.web.rest.client.model.program.specialty.SpecialtyResourcePage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Resource
    TeamProviderTeamLocationService teamProviderTeamLocationService;

    @Resource(name = "programResourceAssembler")
    ResourceAssembler<Program, ProgramResource> programResourceResourceAssembler;

    @Resource(name = "clientTeamLocationResourceAssembler")
    ResourceAssembler<TeamLocation, TeamLocationResource> teamLocationResourceResourceAssembler;

    @Resource(name = "clientTeamProviderResourceAssembler")
    ResourceAssembler<TeamProvider, TeamProviderResource> teamProviderResourceResourceAssembler;

    @Resource(name = "specialtyResourceAssembler")
    ResourceAssembler<Specialty, SpecialtyResource> specialtyResourceAssembler;

    public static final String SPECIALTY_ID_REQUEST_PARAM = "s";

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
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = SPECIALTY_ID_REQUEST_PARAM, value = "the specialty id to narrow by", dataType = "integer", paramType = "query")
    })
    public ProgramResourcePage possiblePrograms(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = {"type.weight", "name"}) Pageable pageable,
            PagedResourcesAssembler<Program> assembler,
            @RequestParam(value = SPECIALTY_ID_REQUEST_PARAM, required = false) Set<Integer> specialtyIds) {

        ProgramSearchFilter programSearchFilter = new ProgramSearchFilter();
        if (!CollectionUtils.isEmpty(specialtyIds)) {
            for (Integer specialtyId : specialtyIds) {
                programSearchFilter.addSpecialty(new Specialty(specialtyId));
            }
        }
        Page<Program> programPage = programService.find(programSearchFilter, pageable);

        return new ProgramResourcePage(programSearchFilter,
                assembler.toResource(programPage, programResourceResourceAssembler),
                programPage);
    }

    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/specialties", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public SpecialtyResourcePage specialties(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<Specialty> assembler){

        Page<Specialty> specialtyPage = programService.findSpecialties(pageable);

        return new SpecialtyResourcePage(
                assembler.toResource(specialtyPage, specialtyResourceAssembler),
                specialtyPage);
    }

    /**
     * This is the request parameter name for the team provider id
     */
    public static final String TEAM_PROVIDER_ID_REQUEST_PARAM = "teamProviderId";

    /**
     * Find possible locations for a team using a team provider as a possible filter
     *
     * @param clientId       the client id
     * @param teamId         the team id
     * @param teamProviderId the team provider id
     * @param pageable       paged request
     * @param assembler      used to create the PagedResources
     * @return TeamLocationResourcePage or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}/locations", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "location.name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = TEAM_PROVIDER_ID_REQUEST_PARAM, value = "the team provider to narrow by", dataType = "string", paramType = "query")
    })
    public ResponseEntity<? extends PagedResource<TeamLocationResource>> locations(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @RequestParam(value = TEAM_PROVIDER_ID_REQUEST_PARAM, required = false) Long teamProviderId,
            @PageableDefault(size = 20, sort = "location.name") Pageable pageable,
            PagedResourcesAssembler<TeamLocation> assembler) {

        TeamLocationResourcePage ret = null;
        boolean searchWithoutProvider = teamProviderId == null;
        if (!searchWithoutProvider) {
            TeamProvider teamProvider = new TeamProvider(teamProviderId);
            // find TPTLs sorted by teamLocation.location.name
            Pageable tptlPageSpec =
                    new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort("teamLocation.location.name"));
            Page<TeamProviderTeamLocation> tptls =
                    teamProviderService.findTeamLocationsByTeamProvider(
                            teamProviderService.reload(teamProvider), tptlPageSpec);
            if (tptls.hasContent()) {
                List<TeamLocation> teamLocationList = new ArrayList<>();
                for (TeamProviderTeamLocation tptl : tptls) {
                    teamLocationList.add(tptl.getTeamLocation());
                }
                Page<TeamLocation> teamLocationPage = new PageImpl<>(teamLocationList, pageable, tptls.getTotalElements());
                ret = new TeamLocationResourcePage(
                        assembler.toResource(teamLocationPage, teamLocationResourceResourceAssembler),
                        teamLocationPage, teamProvider);
            }
            // change to non-provider based search if we are searching the first page
            searchWithoutProvider = !tptlPageSpec.hasPrevious();
        }
        if (ret == null && searchWithoutProvider){
            // not trying to search by provider id or first page of provider based search returned nothing
            Page<TeamLocation> teamLocationPage =
                    teamLocationService.findAllTeamLocationsWithTeam(pageable, new Team(teamId));
            // remove the team provider id from the requested page
            ret = new TeamLocationResourcePage(
                    assembler.toResource(teamLocationPage, teamLocationResourceResourceAssembler),
                    teamLocationPage, null);
        }
        if (ret != null) {
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * This is the request parameter for team location id
     */
    public static final String TEAM_LOCATION_ID_REQUEST_PARAM = "teamLocationId";

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
            "hasPermission(@team.id(#teamId, #clientId), 'PERM_CLIENT_TEAM_SCHEDULE_PROGRAM')")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = TEAM_LOCATION_ID_REQUEST_PARAM, value = "the team location to narrow by", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamProviderResourcePage> providers(
            @PathVariable("clientId") Long clientId,
            @PathVariable("teamId") Long teamId,
            @RequestParam(value = TEAM_LOCATION_ID_REQUEST_PARAM, required = false) Long teamLocationId,
            @PageableDefault(size = 20, sort = "provider.normalizedName") Pageable pageable,
            PagedResourcesAssembler<TeamProvider> assembler) {
        TeamProviderResourcePage ret = null;
        boolean searchWithoutLocation = teamLocationId == null;

        if (!searchWithoutLocation) {
            TeamLocation teamLocation = new TeamLocation(teamLocationId);
            // find TPTLs sorted by teamLocation.provider.normalizedName
            Pageable tptlPageSpec =
                    new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                            new Sort("teamProvider.provider.normalizedName"));
            Page<TeamProviderTeamLocation> tptls =
                    teamProviderTeamLocationService.findByTeamLocation(
                            teamLocationService.reload(teamLocation), tptlPageSpec);
            if (tptls.hasContent()) {
                List<TeamProvider> teamProviders = new ArrayList<>();
                for (TeamProviderTeamLocation tptl : tptls) {
                    teamProviders.add(tptl.getTeamProvider());
                }
                Page<TeamProvider> teamProviderPage =
                        new PageImpl<>(teamProviders, pageable, tptls.getTotalElements());
                ret = new TeamProviderResourcePage(
                        assembler.toResource(teamProviderPage, teamProviderResourceResourceAssembler),
                        teamProviderPage, teamLocation);
            }
            // change to non-provider based search if we are searching the first page
            searchWithoutLocation = !tptlPageSpec.hasPrevious();
        }
        if (ret == null && searchWithoutLocation) {
            // we didn't find any specific providers by location, find all
            Page<TeamProvider> teamProviderPage =
                    teamProviderService.findTeamProvidersByTeam(pageable, new Team(teamId));

            ret = new TeamProviderResourcePage(
                    assembler.toResource(teamProviderPage, teamProviderResourceResourceAssembler),
                    teamProviderPage, null);
        }
        if (ret != null) {
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
