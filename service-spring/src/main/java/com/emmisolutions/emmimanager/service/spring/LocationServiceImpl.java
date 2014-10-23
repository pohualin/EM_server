package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.service.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Location Service Implementation
 */
@Service
public class LocationServiceImpl implements LocationService {

    @Resource
    LocationPersistence locationPersistence;

    @Override
    @Transactional(readOnly = true)
    public Page<Location> list(LocationSearchFilter locationSearchFilter) {
        return list(null, locationSearchFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Location> list(Pageable page, LocationSearchFilter locationSearchFilter) {
        return locationPersistence.list(page, locationSearchFilter);
    }

    @Override
    public Location reload(Location toFind) {
        return locationPersistence.reload(toFind);
    }

    @Override
    @Transactional
    public Location create(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null");
        }
        location.setId(null);
        location.setVersion(null);
        location.setActive(true);
        return locationPersistence.save(location);
    }

    @Override
    @Transactional
    public Location update(Location location) {
        if (location == null || location.getId() == null) {
            throw new IllegalArgumentException("Attempting to update non-persistent location, needs to have an id");
        }
        return locationPersistence.save(location);
    }

}
