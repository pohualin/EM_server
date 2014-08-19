package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

}
