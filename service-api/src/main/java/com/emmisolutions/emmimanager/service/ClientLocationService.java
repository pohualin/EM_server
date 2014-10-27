package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Service API for ClientLocation objects
 */
public interface ClientLocationService {

    /**
     * Find a page of client locations for a client
     *
     * @param client  to find client locations
     * @param pageable which page to fetch
     * @return a page of client location objects
     */
    Page<ClientLocation> find(Client client, Pageable pageable);

    /**
     * Removes a ClientLocation
     *
     * @param id the id of the ClientLocation to delete
     */
    void remove(ClientLocation clientLocation);

    /**
     * Create locations for the client. Think of this as two separate calls.. the save, then
     * a find (above)
     *
     * @param clientId    for which to create them
     * @param locations to associate
     * @param pageable  page to fetch after creation
     * @return a page of persistent locations
     * @see com.emmisolutions.emmimanager.service.ClientLocationService#find(com.emmisolutions.emmimanager.model.Client, org.springframework.data.domain.Pageable)
     */
    Set<ClientLocation> create(Client client, Set<Location> locations);

    /**
     * Load a single client location by id
     *
     * @param clientLocationId the id
     * @return the ClientLocation or null
     */
    ClientLocation reload(ClientLocation clientLocation);

    /**
     * Finds a page of ClientLocation objects that are sparsely popuplated on the Client side
     * of the relationship.
     *
     * @param pageable a page
     * @param locationSearchFilter used to find the locations
     * @return a page of ClientLocation objects, the client relationship could be null
     */
    Page<ClientLocation> findPossibleLocationsToAdd(Client client, LocationSearchFilter locationSearchFilter, Pageable pageable);
}
