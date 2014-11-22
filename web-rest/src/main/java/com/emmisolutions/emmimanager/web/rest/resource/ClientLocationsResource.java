package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.ClientLocationService;
import com.emmisolutions.emmimanager.service.LocationService;
import com.emmisolutions.emmimanager.service.TeamLocationService;
import com.emmisolutions.emmimanager.web.rest.model.clientlocation.ClientLocationFinderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.clientlocation.ClientLocationResource;
import com.emmisolutions.emmimanager.web.rest.model.clientlocation.ClientLocationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.clientlocation.ClientLocationResourcePage;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationResource;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamResourceAssembler;
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
import java.util.HashSet;
import java.util.Set;

import static com.emmisolutions.emmimanager.model.LocationSearchFilter.StatusFilter.fromStringOrActive;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * ClientLocations REST API.
 */
@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class ClientLocationsResource {

    @Resource
    ClientLocationService clientLocationService;

    @Resource
    LocationService locationService;

    @Resource
    LocationResourceAssembler locationResourceAssembler;

    @Resource
    ClientLocationResourceAssembler clientLocationResourceAssembler;

    @Resource
    ClientLocationFinderResourceAssembler clientLocationFinderResourceAssembler;

    @Resource
    TeamLocationService teamLocationService;

    @Resource
    TeamResourceAssembler teamResourceAssembler;

    /**
     * GET to find existing client locations for a client.
     *
     * @param clientId  the client
     * @param pageable  the page to request
     * @param sort      sorting
     * @param assembler used to create the PagedResources
     * @return Page of ClientLocationResource objects or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/locations",
            method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_LIST"})
    @ApiOperation("finds existing ClientLocations")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "location.name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientLocationResourcePage> current(
            @PathVariable Long clientId,
            @PageableDefault(size = 10, sort = "location.name", direction = Sort.Direction.ASC) Pageable pageable,
            Sort sort, PagedResourcesAssembler<ClientLocation> assembler) {
        Page<ClientLocation> clientLocationPage = clientLocationService.findByClient(new Client(clientId), pageable);
        if (clientLocationPage.hasContent()) {
            return new ResponseEntity<>(
                    new ClientLocationResourcePage(assembler.toResource(clientLocationPage, clientLocationResourceAssembler), clientLocationPage, null),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * PUT to update a location entity for a client. This one does not change affect ClientLocation
     * objects just the Location object itself.
     *
     * @param location to update
     * @return LocationResource or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/locations",
            method = RequestMethod.PUT,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @ApiOperation(value = "update a Location using the client", notes = "This method updates the Location, not the ClientLocation. As such, it allows for updates to the belongsTo relationship.")
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_EDIT"})
    public ResponseEntity<LocationResource> update(@PathVariable Long clientId, @RequestBody Location location) {
        location = locationService.update(new Client(clientId), location);
        if (location == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(locationResourceAssembler.toResource(location), HttpStatus.OK);
        }
    }

    /**
     * POST to create a new location and associated it to a client.
     *
     * @param location to create new
     * @return the ClientLocationResource association, success or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/locations",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @ApiOperation(value = "creates a brand new location and associates it to an existing client")
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_CREATE"})
    public ResponseEntity<ClientLocationResource> create(@PathVariable Long clientId, @RequestBody Location location) {
        ClientLocation clientLocation = clientLocationService.createLocationAndAssociateTo(new Client(clientId), location);
        if (clientLocation == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(clientLocationResourceAssembler.toResource(clientLocation), HttpStatus.CREATED);
        }
    }

    /**
     * GET to find all possible locations for a client. The object will come back with a link
     * if it is currently associated to the passed client. If it is not currently in use at the passed client,
     * the link will be null.
     *
     * @param clientId  the client
     * @param pageable  the page to request
     * @param sort      sorting
     * @param assembler used to create the PagedResources
     * @return Page of ClientLocationResource objects or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/locations/associate",
            method = RequestMethod.GET)
    @ApiOperation(value = "finds all possible locations that can be associated to a client", notes = "The object will come back with a link, if it is currently associated to the passed client. If it is not currently in use at the passed client, the link will be null.")
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_LIST"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientLocationResourcePage> possible(
            @PathVariable Long clientId,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            Sort sort, PagedResourcesAssembler<ClientLocation> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "name", required = false) String name) {

        LocationSearchFilter filter = new LocationSearchFilter(fromStringOrActive(status), name);

        Page<ClientLocation> clientLocationPage = clientLocationService.findPossibleLocationsToAdd(
                new Client(clientId), filter, pageable);

        if (clientLocationPage.hasContent()) {
            return new ResponseEntity<>(
                    new ClientLocationResourcePage(assembler.toResource(clientLocationPage, clientLocationFinderResourceAssembler), clientLocationPage, filter),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET to find all possible locations for a client without the ClientLocations associated to this client.
     * The object will come back with a link
     * if it is currently associated to the passed client. If it is not currently in use at the passed client,
     * the link will be null.
     *
     * @param clientId  the client
     * @param pageable  the page to request
     * @param sort      sorting
     * @param assembler used to create the PagedResources
     * @return Page of ClientLocationResource objects or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/locations/associateWithoutCL",
            method = RequestMethod.GET)
    @ApiOperation(value = "finds all possible locations that can be associated to a client", notes = "The object will come back with a link, if it is currently associated to the passed client. If it is not currently in use at the passed client, the link will be null.")
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_LIST"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientLocationResourcePage> possibleWithoutClientLocations(
            @PathVariable Long clientId,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            Sort sort, PagedResourcesAssembler<ClientLocation> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "name", required = false) String name) {

        LocationSearchFilter filter = new LocationSearchFilter(fromStringOrActive(status), name);
        filter.setNotUsingThisClient(new Client(clientId));

        Page<ClientLocation> clientLocationPage = clientLocationService.findPossibleLocationsToAdd(new Client(clientId), filter, pageable);

        if (clientLocationPage.hasContent()) {
            return new ResponseEntity<>(
                    new ClientLocationResourcePage(assembler.toResource(clientLocationPage, clientLocationFinderResourceAssembler), clientLocationPage, filter),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST to create new ClientLocation on a Client with a Set of existing Location objects.
     *
     * @param clientId  on which to create a locations
     * @param locations to attach to the client
     * @return Set of ClientLocationResource objects representing the assocation
     * or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/locations/associate",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @ApiOperation("create a new ClientLocation on a Client for each existing Location in a Set")
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_CREATE"})
    public ResponseEntity<Set<ClientLocationResource>> associate(@PathVariable Long clientId, @RequestBody Set<Location> locations) {
        Set<ClientLocation> clientLocations = clientLocationService.create(new Client(clientId), locations);
        if (clientLocations == null || clientLocations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // convert to resources
            Set<ClientLocationResource> ret = new HashSet<>();
            for (ClientLocation clientLocation : clientLocations) {
                ret.add(clientLocationResourceAssembler.toResource(clientLocation));
            }
            return new ResponseEntity<>(ret, HttpStatus.CREATED);
        }
    }

    /**
     * GET a single client location
     *
     * @param clientId         on which to load a client location
     * @param clientLocationId the actual client location to load
     * @return ClientLocationResource or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/locations/{clientLocationId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_VIEW"})
    @ApiOperation("view a ClientLocation by id")
    public ResponseEntity<ClientLocationResource> view(@PathVariable Long clientId, @PathVariable Long clientLocationId) {
        ClientLocation clientLocation = clientLocationService.reload(new ClientLocation(clientLocationId));
        if (clientLocation != null) {
            return new ResponseEntity<>(clientLocationResourceAssembler.toResource(clientLocation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * DELETE a single client location
     *
     * @param clientId         on which to delete the location association
     * @param clientLocationId the association to remove
     */
    @RequestMapping(value = "/clients/{clientId}/locations/{clientLocationId}", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_DELETE"})
    @ApiOperation("delete a ClientLocation by id")
    public void delete(@PathVariable Long clientId, @PathVariable Long clientLocationId) {
        clientLocationService.remove(new ClientLocation(clientLocationId));
    }

    /**
     * GET a page of Teams associated to the client location
     *
     * @param clientId the client id
     * @param clientLocationId the actual client provider to load
     * @return page of Teams or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/locations/{clientLocationId}/teams", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_PROVIDER_LIST"})
    @ApiOperation("view Teams using ClientProvider")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "team.name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<TeamPage> teams(@PathVariable Long clientId,
                                          @PathVariable Long clientLocationId,
                                          PagedResourcesAssembler<Team> assembler,
                                          @PageableDefault(size = 10, sort = "team.name", direction = Sort.Direction.ASC) Pageable pageable) {
        ClientLocation clientLocation = clientLocationService.reload(new ClientLocation(clientLocationId));
        if (clientLocation != null) {
            Page<Team> teamPage = teamLocationService.findTeamsBy(clientLocation.getClient(), clientLocation.getLocation(), pageable);
            if (teamPage.hasContent()) {
                return new ResponseEntity<>(
                    new TeamPage(assembler.toResource(teamPage, teamResourceAssembler), teamPage, null), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
