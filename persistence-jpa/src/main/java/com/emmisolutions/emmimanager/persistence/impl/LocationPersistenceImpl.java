package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Location Persistence API implementation
 */
@Repository
public class LocationPersistenceImpl implements LocationPersistence {

    @Resource
    LocationRepository locationRepository;

    @Override
    public Page<Location> list(Pageable page) {
        return null;
    }

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location reload(Location location) {
        return null;
    }
}
