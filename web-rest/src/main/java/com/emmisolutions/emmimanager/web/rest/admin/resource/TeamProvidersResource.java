package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.TeamProviderFinderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.TeamProviderPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.TeamProviderResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.TeamProviderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamLocationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamProviderTeamLocationPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamProviderTeamLocationResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Set;

import static com.emmisolutions.emmimanager.model.ProviderSearchFilter.StatusFilter.fromStringOrActive;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * TeamProviders REST API
 */

@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class TeamProvidersResource {

    @Resource
    TeamProviderService teamProviderService;

    @Resource
    TeamLocationResourceAssembler teamLocationResourceAssembler;

    @Resource
    TeamProviderResourceAssembler teamProviderResourceAssembler;

    @Resource
    TeamProviderTeamLocationResourceAssembler teamProviderTeamLocationResourceAssembler;

    @Resource
    TeamProviderFinderResourceAssembler teamProviderFinderResourceAssembler;

    @Resource
    TeamProviderTeamLocationService teamProviderTeamLocationService;

    /**
     * GET for a list of all teamProviders for a given team
     *
     * @param page      paged request
     * @param assembler used to create the PagedResources
     * @param name      a name filter
     * @param status    the status
     * @param teamId    for this team
     * @return ProviderResource
     */
    @RequestMapping(value = "/teams/{teamId}/teamProviders", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<TeamProviderPage> list(
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = {"provider.lastName"}, direction = Sort.Direction.ASC) Pageable page,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<TeamProvider> assembler,
            @RequestParam(value = "name", required = false) String name) {

        Team tofind = new Team();
        tofind.setId(teamId);

        Page<TeamProvider> teamProviderPage = teamProviderService.findTeamProvidersByTeam(page, tofind);
        if (teamProviderPage.hasContent()) {
            return new ResponseEntity<>(new TeamProviderPage(assembler.toResource(teamProviderPage, teamProviderResourceAssembler), teamProviderPage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST to associate list of providers to a given team
     *
     * @param teamId    team to associate providers with
     * @param providers list of teamProviderTeamLocationSaveRequest
     * @return a set of team providers
     */
    @RequestMapping(value = "/teams/{teamId}/teamProviders", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Set<TeamProvider>> associateProvidersToTeam(
            @PathVariable("teamId") Long teamId,
            @RequestBody List<TeamProviderTeamLocationSaveRequest> providers) {

        Team tofind = new Team();
        tofind.setId(teamId);

        Set<TeamProvider> teamProviders = teamProviderService.associateProvidersToTeam(providers, tofind);
        if (!teamProviders.isEmpty()) {
            return new ResponseEntity<>(teamProviders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST to associate list of providers to a given team
     *
     * @param request to update
     * @return ProviderResource
     */
    @RequestMapping(value = "/teamProvider", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<TeamProvider> updateTeamProvider(
            @RequestBody TeamProviderTeamLocationSaveRequest request) {
        teamProviderService.updateTeamProvider(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET to search for TeamLocations
     *
     * @param teamProviderId the team provider id
     * @param pageable       paged request
     * @param sort           sorting request
     * @param status         to filter by
     * @param assembler      used to create the PagedResources
     * @param names          to filter by
     * @return TeamLocationPage or NO_CONTENT
     */
    @RequestMapping(value = "/teamProvider/{teamProviderId}/teamLocations", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamProviderTeamLocationPage> findTeamLocationsByTeamProvider(
            @PathVariable("teamProviderId") Long teamProviderId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Sort sort,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<TeamProviderTeamLocation> assembler,
            @RequestParam(value = "name", required = false) String names) {

        TeamProvider teamProvider = new TeamProvider();
        teamProvider.setId(teamProviderId);
        teamProvider = teamProviderService.reload(teamProvider);

        Page<TeamProviderTeamLocation> tptls = teamProviderService.findTeamLocationsByTeamProvider(teamProvider, pageable);

        if (tptls.hasContent()) {
            return new ResponseEntity<>(new TeamProviderTeamLocationPage(assembler.toResource(tptls, teamProviderTeamLocationResourceAssembler), tptls), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET for teamProvider by id
     *
     * @param teamProviderId the id
     * @return TeamProviderResource or NO_CONTENT on fail
     */
    @RequestMapping(value = "/teamProviders/{teamProviderId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<TeamProviderResource> getById(
            @PathVariable("teamProviderId") Long teamProviderId) {
        TeamProvider teamProvider = new TeamProvider();
        teamProvider.setId(teamProviderId);
        teamProvider = teamProviderService.reload(teamProvider);
        if (teamProvider == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(teamProviderResourceAssembler.toResource(teamProvider), HttpStatus.OK);
        }
    }

    /**
     * GET to find all possible providers that can be used on a team. The object will come back with a link
     * if it is currently associated to the passed team. If it is not currently in use at the passed team,
     * the link will be null.
     *
     * @param teamId    the team
     * @param pageable  the page to request
     * @param sort      sorting
     * @param assembler used to create the PagesResources
     * @param status    filter
     * @param name      filter
     * @return a page of team provider objects
     */
    @RequestMapping(value = "/team/{teamId}/providers/associate",
            method = RequestMethod.GET)
    @ApiOperation(value = "finds all possible providers that can be associated to a team", notes = "The object will come back with a link, if it is currently associated to the passed team. If it is not currently in use at the passed team, the link will be null.")
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamProviderPage> possible(
            @PathVariable Long teamId,
            @PageableDefault(size = 10, sort = {"lastName"}, direction = Sort.Direction.ASC) Pageable pageable,
            Sort sort, PagedResourcesAssembler<TeamProvider> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "name", required = false) String name) {

        ProviderSearchFilter filter = new ProviderSearchFilter(fromStringOrActive(status), name);

        Page<TeamProvider> teamProviderPage = teamProviderService.findPossibleProvidersToAdd(
                new Team(teamId), filter, pageable);

        if (teamProviderPage.hasContent()) {
            return new ResponseEntity<>(
                    new TeamProviderPage(assembler.toResource(teamProviderPage, teamProviderFinderResourceAssembler), teamProviderPage, filter),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * DELETE for deletion of a teamProvider from a team
     *
     * @param teamProviderId to delete
     */
    @RequestMapping(value = "/teamProviders/{teamProviderId}", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public void deleteProviderFromTeamProvider(@PathVariable Long teamProviderId) {
        TeamProvider teamProvider = new TeamProvider();
        teamProvider.setId(teamProviderId);
        teamProviderService.delete(teamProvider);
    }

    /**
     * POST to create teamProviderTeamLocation
     *
     * @return the newly created team provider team location objects
     */
    @RequestMapping(value = "/teamProviders/{teamProviderId}/teamProviderTeamLocation", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public Set<TeamProviderTeamLocation> createTeamProviderTeamLocation(
            @RequestBody Set<TeamLocation> teamLocations,
            @PathVariable("teamProviderId") Long teamProviderId) {

        TeamProvider toFind = new TeamProvider();
        toFind.setId(teamProviderId);
        return teamProviderTeamLocationService.createTeamProviderTeamLocation(teamLocations, toFind);
    }

    /**
     * GET for teamProvider by provider and team
     *
     * @param teamId     to find by
     * @param providerId to find by
     * @return TeamProviderResource or NO_CONTENT on fail
     */
    @RequestMapping(value = "/teamProviders/{teamId}/teamProviderByProvider", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<TeamProviderResource> getByProviderAndTeam(
            @PathVariable("teamId") Long teamId,
            @RequestParam(value = "providerId", required = false) Long providerId) {

        Team toFind = new Team();
        toFind.setId(teamId);

        Provider providerToFind = new Provider();
        providerToFind.setId(providerId);

        TeamProvider teamProvider = teamProviderService.findTeamProviderByProviderAndTeam(null, providerToFind, toFind);
        if (teamProvider == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(teamProviderResourceAssembler.toResource(teamProvider), HttpStatus.OK);
        }
    }
}
