package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.service.LocationService;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationPage;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationReferenceData;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationResource;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationResourceAssembler;
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

import static com.emmisolutions.emmimanager.model.LocationSearchFilter.StatusFilter.fromStringOrAll;
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
            @PageableDefault(size = 50) Pageable pageable,
            @SortDefault(sort = "id") Sort sort,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<Location> assembler,
            @RequestParam(value = "name", required = false) String... names) {

        // create the search filter
        LocationSearchFilter locationSearchFilter = new LocationSearchFilter(fromStringOrAll(status), names);

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
