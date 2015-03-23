package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.service.LocationService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
            throw new InvalidDataAccessApiUsageException("location cannot be null");
        }
        location.setId(null);
        location.setVersion(null);
        location.setBelongsTo(null);
        return locationPersistence.save(location);
    }

    @Override
    @Transactional
    public Location create(Client client, Location location) {
        if (location == null) {
            throw new InvalidDataAccessApiUsageException("location cannot be null");
        }
        Client belongsTo = location.getBelongsTo();
        Location persistent = create(location);
        persistent.setBelongsTo(belongsTo);
        return update(client, persistent);
    }

    @Override
    @Transactional
    public Location update(Client client, Location location) {
        Location dbLocation = locationPersistence.reload(location);
        if (dbLocation == null) {
            throw new InvalidDataAccessApiUsageException("Attempting to update non-persistent location, needs to have an id");
        }
        if (client == null) {
            if (dbLocation.getBelongsTo() != null) {
                // can't update belongsTo without a client id
                location.setBelongsTo(dbLocation.getBelongsTo());
            }
        } else if (dbLocation.getBelongsTo() != null) {
            // belongsTo was already set
            if (!client.equals(dbLocation.getBelongsTo())) {
                // not allowed to modify, clients are different
                location.setBelongsTo(dbLocation.getBelongsTo());
            }
        }
        return locationPersistence.save(location);
    }

}
