package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Location Persistence API
 */
public interface LocationPersistence {

    Page<Location> list(Pageable page, LocationSearchFilter filter);

    Location save(Location location);

    Location reload(Location location);
}
