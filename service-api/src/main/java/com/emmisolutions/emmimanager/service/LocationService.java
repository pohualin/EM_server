package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocationModificationRequest;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Location Services
 */
public interface LocationService {

    /**
     * Get the first page (default size) of location objects based upon the filter
     *
     * @param locationSearchFilter or null
     * @return the first page of location objects
     */
    Page<Location> list(LocationSearchFilter locationSearchFilter);

    /**
     * Find all IDs associated to this client
     * @param clientId to find for
     * @return Set of all ids
     */
    Set<Long> list(Long clientId);

    /**
     * Get a page of location objects.
     *
     * @param page                 to retrieve
     * @param locationSearchFilter filtered by
     * @return a page of location objects
     */
    Page<Location> list(Pageable page, LocationSearchFilter locationSearchFilter);

    /**
     * Reloads a location from persistent storage
     *
     * @param location to reload
     * @return the reloaded location
     */
    Location reload(Location toFind);

    /**
     * Creates/Persists a location
     *
     * @param location to save
     * @return the persistent location
     */
    Location create(Location location);

    /**
     * This update is for all attributes on the client but not the relationship to clients.
     * E.g. it wouldn't update which client the location belongs to nor the clients which
     * use this location
     *
     * @param location to update the properties
     * @return the updated location
     */
    Location update(Location location);

    /**
     * Update the relationships to client for the locations within the modification request
     *
     * @param toUpdate            to modify
     * @param modificationRequest to update the locations
     */
    void updateClientLocations(Client toUpdate, ClientLocationModificationRequest modificationRequest);

}
