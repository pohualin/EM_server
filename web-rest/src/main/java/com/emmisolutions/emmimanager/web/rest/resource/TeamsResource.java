package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.web.rest.model.team.ReferenceData;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamResource;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static com.emmisolutions.emmimanager.model.TeamSearchFilter.StatusFilter.fromStringOrActive;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Teams REST API
 */
@RestController
@RequestMapping(value = "/webapi",
    produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class TeamsResource {

    @Resource
    TeamService teamService;

    @Resource
    TeamResourceAssembler teamResourceAssembler;

    @Resource
    ClientService clientService;

    /**
     * GET a single team
     *
     * @param clientId to use
     * @param id       to load
     * @return ClientResource or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_VIEW"})
    public ResponseEntity<TeamResource> getTeam(@PathVariable Long clientId, @PathVariable("id") Long id) {
        Team toFind = new Team();
        toFind.setId(id);
        toFind = teamService.reload(toFind);
        if (toFind != null) {
            return new ResponseEntity<>(teamResourceAssembler.toResource(toFind), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * PUT to update a team
     *
     * @param clientId on this client
     * @param teamId   for this team
     * @param team     the updated body
     * @return updated TeamResource
     */
    @RequestMapping(value = "/clients/{clientId}/teams/{teamId}", method = RequestMethod.PUT,
        consumes = {APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_EDIT"})
    public ResponseEntity<TeamResource> updateTeam(@PathVariable Long clientId, @PathVariable Long teamId, @RequestBody Team team) {
        Team updated = teamService.update(team);
        if (updated != null) {
            return new ResponseEntity<>(teamResourceAssembler.toResource(updated), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET to search for teams
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param status    to filter by
     * @param assembler used to create the PagedResources
     * @param names     to filter by
     * @return LocationPage or NO_CONTENT
     */
    @RequestMapping(value = "/teams",
        method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_LIST"})
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamPage> list(
        @PageableDefault(size = 10, sort = "name") Pageable pageable,
        @SortDefault(sort = "id") Sort sort,
        @RequestParam(value = "status", required = false) String status,
        PagedResourcesAssembler<Team> assembler,
        @RequestParam(value = "name", required = false) String... names) {

        return findTeams(pageable, assembler, null, status, names);
    }

    /**
     * GET to search for teams on a client
     *
     * @param clientId  the client id
     * @param pageable  paged request
     * @param sort      sorting request
     * @param status    to filter by
     * @param assembler used to create the PagedResources
     * @param names     to filter by
     * @return LocationPage or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/teams", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_LIST"})
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "50", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamPage> clientTeams(@PathVariable Long clientId,
                                                @PageableDefault(size = 50) Pageable pageable,
                                                @SortDefault(sort = "id") Sort sort,
                                                @RequestParam(value = "status", required = false) String status,
                                                PagedResourcesAssembler<Team> assembler,
                                                @RequestParam(value = "name", required = false) String... names) {
        return findTeams(pageable, assembler, clientId, status, names);
    }

    /**
     * POST to create a new team
     *
     * @param clientId to use
     * @param team     to create
     * @return TeamResource or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/teams",
        method = RequestMethod.POST,
        consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_CREATE"})
    public ResponseEntity<TeamResource> createTeam(@PathVariable Long clientId, @RequestBody Team team) {
        Client client = new Client();
        client.setId(clientId);
        client = clientService.reload(client);

        team.setClient(client);
        team = teamService.create(team);
        if (team == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(teamResourceAssembler.toResource(team), HttpStatus.CREATED);
        }
    }

    /**
     * GET to Retrieve reference data about teams.
     *
     * @return ReferenceData
     */
    @RequestMapping(value = "/teams/ref", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_CREATE", "PERM_TEAM_EDIT"})
    public ReferenceData getReferenceData() {
        return new ReferenceData();
    }

    private ResponseEntity<TeamPage> findTeams(Pageable pageable, PagedResourcesAssembler<Team> assembler, Long clientId, String status, String... names) {
        // create the search filter
        TeamSearchFilter teamSearchFilter = new TeamSearchFilter(clientId, fromStringOrActive(status), names);

        // find the page of clients
        Page<Team> teamPage = teamService.list(pageable, teamSearchFilter);

        if (teamPage.hasContent()) {
            // create a TeamPage containing the response
            return new ResponseEntity<>(
                new TeamPage(assembler.toResource(teamPage, teamResourceAssembler),
                    teamPage, teamSearchFilter),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Finds a team by its normalized name
     *
     * @param normalizedName to search with
     * @param pageable       specification
     * @param clientId       to use
     * @return a TeamResource if found or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/teams/findNormalizedName", method = RequestMethod.GET)
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "1", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamResource> findByNormalizedNameForClient(
        @RequestParam(value = "normalizedName", required = false) String normalizedName,
        @PageableDefault(size = 1) Pageable pageable,
        @PathVariable("clientId") Long clientId) {

        Team toFind = teamService.findByNormalizedNameAndClientId(normalizedName, clientId);
        if (toFind != null) {
            return new ResponseEntity<>(
                teamResourceAssembler.toResource(toFind), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
