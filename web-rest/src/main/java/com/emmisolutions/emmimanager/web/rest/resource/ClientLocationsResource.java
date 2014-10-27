package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.service.ClientLocationService;
import com.emmisolutions.emmimanager.service.LocationService;
import com.emmisolutions.emmimanager.web.rest.model.clientlocation.ClientLocationFinderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.clientlocation.ClientLocationResource;
import com.emmisolutions.emmimanager.web.rest.model.clientlocation.ClientLocationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.clientlocation.ClientLocationResourcePage;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationResource;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "createdDate,desc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientLocationResourcePage> current(
            @PathVariable Long clientId,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            Sort sort, PagedResourcesAssembler<ClientLocation> assembler) {
        Page<ClientLocation> clientLocationPage = clientLocationService.find(new Client(clientId), pageable);
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
     * PUT to update a location for a client.
     *
     * @param location to update
     * @return LocationResource or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/locations",
            method = RequestMethod.PUT,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
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
     * POST to create a new location for a client.
     *
     * @param location to update
     * @return LocationResource or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/locations",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_CREATE"})
    public ResponseEntity<LocationResource> create(@PathVariable Long clientId, @RequestBody Location location) {
        location = locationService.create(new Client(clientId), location);
        if (location == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(locationResourceAssembler.toResource(location), HttpStatus.OK);
        }
    }

    /**
     * GET to find all possible locations for a client. The object will come back with the
     * client portion attached only if it is currently in use by the passed client. If it
     * is not currently in use at the passed client, the client will be blank.
     *
     * @param clientId  the client
     * @param pageable  the page to request
     * @param sort      sorting
     * @param assembler used to create the PagedResources
     * @return Page of ClientLocationResource objects or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/locations/associate",
            method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_LIST"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "createdDate,desc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientLocationResourcePage> possible(
            @PathVariable Long clientId,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
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
     * POST to create new ClientLocation
     *
     * @param client    to use
     * @param pageable  the page specs
     * @param assembler to assemble the response
     * @param clientId  to use
     * @param locations to attach to the client
     * @param sort      the sorting of the return
     * @return Page of ClientLocationResource or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/clients/{clientId}/locations/associate",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_CREATE"})
    public ResponseEntity<ClientLocationResourcePage> associate(
            @PathVariable Long clientId, @RequestBody Set<Location> locations,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            Sort sort, PagedResourcesAssembler<ClientLocation> assembler) {
        Set<ClientLocation> clientLocations = clientLocationService.create(new Client(clientId), locations);
        if (clientLocations == null || clientLocations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return current(clientId, pageable, sort, assembler);
        }
    }

    /**
     * GET a single client location
     *
     * @param id to load
     * @return ClientLocationResource or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/locations/{clientLocationId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_VIEW"})
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
     * @param id to delete
     */
    @RequestMapping(value = "/clients/{clientId}/locations/{clientLocationId}", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_DELETE"})
    public void delete(@PathVariable Long clientId, @PathVariable Long clientLocationId) {
        clientLocationService.remove(new ClientLocation(clientLocationId));
    }
}
