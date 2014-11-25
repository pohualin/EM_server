package com.emmisolutions.emmimanager.web.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamLocationSearchFilter;
import com.emmisolutions.emmimanager.model.TeamLocationTeamProviderSaveRequest;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.service.TeamLocationService;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamLocationPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamLocationResource;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamLocationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamProviderTeamLocationPage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

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
    TeamService teamService;
    
	@Resource
	TeamProviderTeamLocationService teamProviderTeamLocationService;

    /**
     * GET to search for TeamLocations
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param status    to filter by
     * @param assembler used to create the PagedResources
     * @param names     to filter by
     * @return TeamLocationPage or NO_CONTENT
     */
    @RequestMapping(value = "/teams/{teamId}/locations", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_LOCATION_LIST"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="size", defaultValue="10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="sort", defaultValue="id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamLocationPage> list(
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "location.name", direction = Sort.Direction.ASC) Pageable pageable,
            Sort sort,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<TeamLocation> assembler,
            @RequestParam(value = "name", required = false) String names) {

        Team toFind = new Team();
        toFind.setId(teamId);
        toFind = teamService.reload(toFind);
        TeamLocationSearchFilter teamLocationSearchFilter = new TeamLocationSearchFilter(teamId);

        Page<TeamLocation> teamLocationPage = teamLocationService.findAllTeamLocationsWithTeam(pageable,toFind);

        if (teamLocationPage.hasContent()) {
            PagedResources<TeamLocationResource> teamLocationResourceSupports = assembler.toResource(teamLocationPage, teamLocationResourceAssembler);
            TeamLocationPage teamLocationPage1 = new TeamLocationPage(teamLocationResourceSupports, teamLocationPage, teamLocationSearchFilter);
            return new ResponseEntity<>(teamLocationPage1,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/teams/{teamLocationId}/tptl", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_LOCATION_LIST"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="size", defaultValue="10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name="sort", defaultValue="id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamProviderTeamLocationPage> listTeamProviderTeamLocation(
            @PathVariable("teamLocationId") Long teamLocationId,
            @PageableDefault(size = 10, sort = "location.name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<TeamProviderTeamLocation> assembler) {

        TeamLocation toFind = new TeamLocation();
        toFind.setId(teamLocationId);
        toFind = teamLocationService.reload(toFind);

        Page<TeamProviderTeamLocation> tptlPage = teamProviderTeamLocationService.findByTeamLocation(toFind, pageable);
        
        if (tptlPage.hasContent()) {
        	TeamProviderTeamLocationPage teamLocationPage1 = new TeamProviderTeamLocationPage(null, tptlPage);
            return new ResponseEntity<>(teamLocationPage1,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    /**
     * POST to create new Team, Location association
     *
     * @param teamId to associate locations with
     * @param locationSet to associate with team
     * @return TeamLocationResource or INTERNAL_SERVER_ERROR if it could not be saved
     */
    @RequestMapping(value = "/teams/{teamId}/locations", method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_LOCATION_CREATE"})
    public void create(@PathVariable("teamId") Long teamId,@RequestBody Set<TeamLocationTeamProviderSaveRequest> reqs) {
        Team toFind = new Team();
        toFind.setId(teamId);
        teamLocationService.save(toFind,reqs);
    }

    /**
     * GET to get a teamLocation
     *
     * @param teamId to get
     * @param teamLocationId to get
     * @return TeamLocationResource or NO_CONTENT
     */
    @RequestMapping(value = "/teams/{teamId}/locations/{teamLocationId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_LOCATION_VIEW"})
    public ResponseEntity<TeamLocationResource> getTeamLocation(@PathVariable("teamId") Long teamId,@PathVariable("teamLocationId") Long teamLocationId) {
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
     * @param teamId to get
     * @param teamLocationId to get
     *
     */
	@RequestMapping(value = "/teams/{teamId}/locations/{teamLocationId}", method = RequestMethod.DELETE)
	@RolesAllowed({ "PERM_GOD", "PERM_TEAM_LOCATION_DELETE" })
	public ResponseEntity<TeamLocationResource> deleteTeamLocation(@PathVariable("teamId") Long teamId,@PathVariable("teamLocationId") Long teamLocationId) {
        TeamLocation toFind = new TeamLocation();
        toFind.setId(teamLocationId);
		teamLocationService.delete(toFind);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
