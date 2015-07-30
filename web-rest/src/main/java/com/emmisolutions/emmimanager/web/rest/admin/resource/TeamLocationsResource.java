package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.TeamLocationService;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.*;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * TeamLocations REST API.
 */
@RestController
@RequestMapping(value = "/webapi",
    produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class TeamLocationsResource {

    @Resource
    TeamLocationService teamLocationService;

    @Resource
    TeamLocationResourceAssembler teamLocationResourceAssembler;

    @Resource
    TeamProviderTeamLocationResourceAssembler teamProviderTeamLocationResourceAssembler;

    @Resource
    TeamService teamService;

    @Resource
    TeamProviderTeamLocationService teamProviderTeamLocationService;

    @Resource
    ResourceAssembler<TeamLocation, TeamLocationResource> teamLocationFinderResourceAssembler;

    /**
     * GET to search for TeamLocations
     *
     * @param teamId    the team id
     * @param pageable  paged request
     * @param assembler used to create the PagedResources
     * @return TeamLocationPage or NO_CONTENT
     */
    @RequestMapping(value = "/teams/{teamId}/locations", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "location.name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamLocationPage> list(
        @PathVariable("teamId") Long teamId,
        @PageableDefault(size = 10, sort = "location.name", direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler<TeamLocation> assembler) {

        Team toFind = new Team();
        toFind.setId(teamId);
        toFind = teamService.reload(toFind);
        TeamLocationSearchFilter teamLocationSearchFilter = new TeamLocationSearchFilter(teamId);

        Page<TeamLocation> teamLocationPage = teamLocationService.findAllTeamLocationsWithTeam(pageable, toFind);

        if (teamLocationPage.hasContent()) {
            PagedResources<TeamLocationResource> teamLocationResourceSupports = assembler.toResource(teamLocationPage, teamLocationResourceAssembler);
            TeamLocationPage teamLocationPage1 = new TeamLocationPage(teamLocationResourceSupports, teamLocationPage, teamLocationSearchFilter);
            return new ResponseEntity<>(teamLocationPage1, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets TPTLs for a location
     *
     * @param teamLocationId the team location
     * @param pageable       specification
     * @param assembler      to make the resource
     * @return a TeamProviderTeamLocationPage object
     */
    @RequestMapping(value = "/teams/{teamLocationId}/tptl", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamProviderTeamLocationPage> listTeamProviderTeamLocation(
        @PathVariable("teamLocationId") Long teamLocationId,
        @PageableDefault(size = 10, sort = "teamProvider.provider.lastName", direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler<TeamProviderTeamLocation> assembler) {

        TeamLocation toFind = new TeamLocation();
        toFind.setId(teamLocationId);
        toFind = teamLocationService.reload(toFind);

        Page<TeamProviderTeamLocation> tptlPage = teamProviderTeamLocationService.findByTeamLocation(toFind, pageable);

        if (tptlPage.hasContent()) {
            PagedResources<TeamProviderTeamLocationResource> tptlResourceSupports = assembler.toResource(tptlPage, teamProviderTeamLocationResourceAssembler);
            TeamProviderTeamLocationPage teamLocationPage1 = new TeamProviderTeamLocationPage(tptlResourceSupports, tptlPage);
            return new ResponseEntity<>(teamLocationPage1, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Update team locations
     *
     * @param teamLocationId to updated
     * @param req            update request
     */
    @RequestMapping(value = "/teams/{teamLocationId}/tptl", method = RequestMethod.POST,
        consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public void updateTeamProviderTeamLocation(
        @PathVariable("teamLocationId") Long teamLocationId, @RequestBody TeamLocationTeamProviderSaveRequest req) {

        TeamLocation toFind = new TeamLocation();
        toFind.setId(teamLocationId);
        teamProviderTeamLocationService.updateTeamProviderTeamLocations(toFind, req);
    }

    /**
     * POST to create new TeamLocation, providers association
     *
     * @param teamId    to associate locations with
     * @param reqs      to associate with team
     * @return TeamProviderTeamLocationPage or INTERNAL_SERVER_ERROR if it could not be saved
     */
    @RequestMapping(value = "/teams/{teamId}/locations", method = RequestMethod.POST,
        consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Set<TeamProviderTeamLocationResource>> create(
        @PathVariable("teamId") Long teamId, @RequestBody Set<TeamLocationTeamProviderSaveRequest> reqs) {
        Team toFind = new Team();
        toFind.setId(teamId);
        Set<TeamProviderTeamLocation> saved = teamLocationService.save(toFind, reqs);

        // convert to resources
        Set<TeamProviderTeamLocationResource> ret = new HashSet<>();
        for (TeamProviderTeamLocation tptl : saved) {
            ret.add(teamProviderTeamLocationResourceAssembler.toResource(tptl));
        }
        return new ResponseEntity<>(ret, HttpStatus.CREATED);        
    }

    /**
     * GET to get a teamLocation
     *
     * @param teamId         to get
     * @param teamLocationId to get
     * @return TeamLocationResource or NO_CONTENT
     */
    @RequestMapping(value = "/teams/{teamId}/locations/{teamLocationId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<TeamLocationResource> getTeamLocation(@PathVariable("teamId") Long teamId, @PathVariable("teamLocationId") Long teamLocationId) {
        TeamLocation toFind = new TeamLocation();
        toFind.setId(teamId);
        toFind = teamLocationService.reload(toFind);
        if (toFind != null) {
            return new ResponseEntity<>(teamLocationResourceAssembler.toResource(toFind), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * delete a team location
     *
     * @param teamId         to get
     * @param teamLocationId to get
     * @return OK
     */
    @RequestMapping(value = "/teams/{teamId}/locations/{teamLocationId}", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<TeamLocationResource> deleteTeamLocation(@PathVariable("teamId") Long teamId, @PathVariable("teamLocationId") Long teamLocationId) {
        TeamLocation toFind = new TeamLocation();
        toFind.setId(teamLocationId);
        teamLocationService.delete(toFind);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Associate all client locations to team with exclude list.
     *
     * @param teamId     the id of the team to associate to
     * @param excludeSet a set of location ids to be excluded
     * @return OK
     */
    @RequestMapping(value = "/teams/{teamId}/associate_all_client_locations_except/", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Void> associateAllClientLocationsExcept(
            @PathVariable("teamId") Long teamId,
            @RequestBody Set<Long> excludeSet) {
        teamLocationService.associateAllClientLocationsExcept(new Team(teamId),
                excludeSet);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Find possible ClientLocations that can be associate with the team
     *
     * @param teamId    the id of the team to associate to
     * @param pageable  to use
     * @param assembler to use
     * @return a page of TeamLocationResource with OK or NO_CONTENT
     */
    @RequestMapping(value = "/teams/{teamId}/locations/associate", method = RequestMethod.GET)
    @ApiOperation(value = "finds all possible client locations that can be associated to a team", notes = "The object will come back with a link, if it is currently associated to the passed team. If it is not currently in use at the passed team, the link will be null.")
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "location.name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")})
    public ResponseEntity<TeamLocationResourcePage> possible(
            @PathVariable Long teamId,
            @PageableDefault(size = 10, sort = "location.name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<TeamLocation> assembler) {

        Page<TeamLocation> teamLocationPage = teamLocationService
                .findPossibleClientLocationsToAdd(new Team(teamId), pageable);

        if (teamLocationPage.hasContent()) {
            return new ResponseEntity<>(new TeamLocationResourcePage(
                    assembler.toResource(teamLocationPage,
                            teamLocationFinderResourceAssembler),
                    teamLocationPage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
