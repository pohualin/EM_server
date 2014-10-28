package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Location Persistence API
 */
public interface LocationPersistence {

    /**
     * Get a page of locations
     *
     * @param page   to fetch
     * @param filter to filter
     * @return page of location objects
     */
    Page<Location> list(Pageable page, LocationSearchFilter filter);

    /**
     * Save a location
     *
     * @param location to be saved
     * @return saved location
     */
    Location save(Location location);

    /**
     * re-load location from persistence
     *
     * @param location to load
     * @return the location
     */
    Location reload(Location location);

}
