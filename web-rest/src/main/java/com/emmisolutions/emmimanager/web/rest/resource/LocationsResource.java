package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocationModificationRequest;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.service.LocationService;
import com.emmisolutions.emmimanager.web.rest.model.location.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.ResourceAssembler;
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
 * Locations REST API
 */
@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class LocationsResource {

    @Resource
    LocationResourceAssembler locationResourceAssembler;

    @Resource
    LocationService locationService;

    /**
     * GET a single location
     *
     * @param id to load
     * @return ClientResource or NO_CONTENT
     */
    @RequestMapping(value = "/locations/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_LOCATION_VIEW"})
    public ResponseEntity<LocationResource> get(@PathVariable("id") Long id) {
        Location toFind = new Location();
        toFind.setId(id);
        toFind = locationService.reload(toFind);
        if (toFind != null) {
            return new ResponseEntity<>(locationResourceAssembler.toResource(toFind), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST to create new client
     *
     * @param client to create
     * @return ClientResource or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/locations",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_LOCATION_CREATE"})
    public ResponseEntity<LocationResource> create(@RequestBody Location location) {
        location = locationService.create(location);
        if (location == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(locationResourceAssembler.toResource(location), HttpStatus.CREATED);
        }
    }

    /**
     * POST to create new client
     *
     * @param client to create
     * @return ClientResource or INTERNAL_SERVER_ERROR if it could not be created
     */
    @RequestMapping(value = "/locations",
            method = RequestMethod.PUT,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
    )
    @RolesAllowed({"PERM_GOD", "PERM_LOCATION_EDIT"})
    public ResponseEntity<LocationResource> update(@RequestBody Location location) {
        location = locationService.update(location);
        if (location == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(locationResourceAssembler.toResource(location), HttpStatus.CREATED);
        }
    }

    /**
     * GET to search for locations
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param status    to filter by
     * @param assembler used to create the PagedResources
     * @param names     to filter by
     * @return LocationPage or NO_CONTENT
     */
    @RequestMapping(value = "/locations",
            method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_LOCATION_LIST"})
    public ResponseEntity<LocationPage> list(
            @PageableDefault(size = 10) Pageable pageable,
            @SortDefault(sort = {"name","state"}) Sort sort,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<Location> assembler,
            @RequestParam(value = "name", required = false) String... names) {

        return findLocations(pageable, assembler, locationResourceAssembler,  null, status, names);
    }

    /**
     * GET to search for location on a client
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param status    to filter by
     * @param assembler used to create the PagedResources
     * @param names     to filter by
     * @return LocationPage or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/locations",
            method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_LIST"})
    public ResponseEntity<LocationPage> clientLocations(
            @PathVariable Long clientId,
            @PageableDefault(size = 50, sort = {"name"} ) Pageable pageable,
            Sort sort,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<Location> assembler,
            @RequestParam(value = "name", required = false) String... names) {

        return findLocations(pageable, assembler, new ClientLocationResourceAssembler(clientId), clientId, status, names);
    }

    /**
     * Fetch all of the location IDs associated with a client
     * @param clientId to find all of the IDs for
     * @return a Set of IDs
     */
    @RequestMapping(value = "/clients/{clientId}/locations/all",
            method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_LIST"})
    public Set<Long> allClientLocations(
            @PathVariable Long clientId) {

        return locationService.list(clientId);
    }

    /**
     * POST to update the locations on a client
     */
    @RequestMapping(value = "/clients/{clientId}/locations",
            method = RequestMethod.PUT,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_EDIT"})
    public void modifyClientLocations(@PathVariable Long clientId, @RequestBody ClientLocationModificationRequest modificationRequest) {
        Client client = new Client();
        client.setId(clientId);
        locationService.updateClientLocations(client, modificationRequest);
    }

    /**
     * DELETE to remove a location on a client
     */
    @RequestMapping(value = "/clients/{clientId}/locations/{locationId}",
            method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_EDIT"})
    public ResponseEntity<LocationResource> getLocationAtClient(@PathVariable Long clientId, @PathVariable Long locationId) {
        Client client = new Client();
        client.setId(clientId);
        Location location = new Location();
        location.setId(locationId);
        Location ret = locationService.reloadLocationUsedByClient(client, location);
        if (ret != null){
             return new ResponseEntity<>(new ClientLocationResourceAssembler(clientId).toResource(ret),HttpStatus.OK);
        }    else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * DELETE to remove a location on a client
     */
    @RequestMapping(value = "/clients/{clientId}/locations/{locationId}",
            method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_LOCATION_EDIT"})
    public void deleteClientLocation(@PathVariable Long clientId, @PathVariable Long locationId) {
        Client client = new Client();
        client.setId(clientId);
        Location location = new Location();
        location.setId(locationId);
        locationService.delete(client, location);
    }

    private ResponseEntity<LocationPage> findLocations(Pageable pageable, PagedResourcesAssembler<Location> assembler,
                                                       ResourceAssembler<Location, LocationResource> locationResourceAssembler,
                                                       Long clientId, String status, String... names) {
        // create the search filter
        LocationSearchFilter locationSearchFilter = new LocationSearchFilter(clientId, fromStringOrActive(status), names);

        // find the page of clients
        Page<Location> locationPage = locationService.list(pageable, locationSearchFilter);

        if (locationPage.hasContent()) {
            // create a LocationPage containing the response
            return new ResponseEntity<>(
                    new LocationPage(assembler.toResource(locationPage, locationResourceAssembler),
                            locationPage, locationSearchFilter),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET to Retrieve reference data about clients.
     *
     * @return ReferenceData
     */
    @RequestMapping(value = "/locations/ref", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_LOCATION_LIST", "PERM_LOCATION_EDIT"})
    public LocationReferenceData getReferenceData() {
        return new LocationReferenceData();
    }

}
