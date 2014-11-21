package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Persistence API for ClientLocation objects
 */
public interface ClientLocationPersistence {

    /**
     * Find a page of client locations for a client
     *
     * @param clientId   to find client locations
     * @param pageable which page to fetch
     * @return a page of client location objects
     */
    Page<ClientLocation> findByClient(Long clientId, Pageable pageable);

    /**
     * Find a page of client locations for a location
     *
     * @param locationId   to find client locations
     * @param pageable which page to fetch
     * @return a page of client location objects
     */
    Page<ClientLocation> findByLocation(Long locationId, Pageable pageable);

    /**
     * Creates a ClientLocation from a Location and a Client
     *
     * @param locationId to use
     * @param clientId   to use
     * @return saved ClientLocation
     */
    ClientLocation create(Long locationId, Long clientId);

    /**
     * Removes a ClientLocation from the db
     *
     * @param id to delete
     */
    void remove(Long id);

    /**
     * Loads a ClientLocation from the database
     *
     * @param clientLocationId to load
     * @return a ClientLocation or {@literal null}
     */
    ClientLocation reload(Long clientLocationId);

    /**
     * Load matching ClientLocation objects where the Location is within one of the
     * matched locations
     * @param clientId to use
     * @param matchedLocations to filter by
     * @return the List of ClientLocations matching both the client id and page of locations, never null
     */
    List<ClientLocation> load(Long clientId, Page<Location> matchedLocations);

    /**
     * Loads a ClientLocation from a Location and a Client
     *
     * @param locationId to use
     * @param clientId   to use
     * @return saved ClientLocation or null
     */
    ClientLocation reload(Long locationId, Long clientId);
}
