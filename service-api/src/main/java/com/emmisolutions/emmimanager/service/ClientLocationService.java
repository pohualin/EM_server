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
     * @param client   to find client locations
     * @param pageable which page to fetch
     * @return a page of client location objects
     */
    Page<ClientLocation> findByClient(Client client, Pageable pageable);

    /**
     * Find a page of client locations for a location
     *
     * @param location to find client locations
     * @param pageable which page to fetch
     * @return a page of client location objects
     */
    Page<ClientLocation> findByLocation(Location location, Pageable pageable);

    /**
     * Removes a ClientLocation
     *
     * @param clientLocation the ClientLocation to delete
     */
    void remove(ClientLocation clientLocation);

    /**
     * Create locations for the client.
     *
     * @param client    for which to create them
     * @param locations to associate
     * @return the set of saved locations
     */
    Set<ClientLocation> create(Client client, Set<Location> locations);

    /**
     * Creates a brand new location as well as a new client location
     *
     * @param client   to use
     * @param location to create/save and associate to the client
     * @return saved ClientLocation
     */
    ClientLocation createLocationAndAssociateTo(Client client, Location location);

    /**
     * Load a single client location by id
     *
     * @param clientLocation the to reload
     * @return the ClientLocation or null
     */
    ClientLocation reload(ClientLocation clientLocation);

    /**
     * Finds a page of ClientLocation objects that are sparsely populated on the Client side
     * of the relationship.
     *
     * @param pageable             a page
     * @param locationSearchFilter used to find the locations
     * @param client               on which we are adding
     * @return a page of ClientLocation objects, the client relationship could be null
     */
    Page<ClientLocation> findPossibleLocationsToAdd(Client client, LocationSearchFilter locationSearchFilter, Pageable pageable);
}
