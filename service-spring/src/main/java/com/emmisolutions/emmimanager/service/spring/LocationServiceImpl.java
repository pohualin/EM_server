package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocationModificationRequest;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.service.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Location Service Implementation
 */
@Service
public class LocationServiceImpl implements LocationService {

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Override
    @Transactional(readOnly = true)
    public Page<Location> list(LocationSearchFilter locationSearchFilter) {
        return list(null, locationSearchFilter);
    }

    @Override
    public Set<Long> list(Long clientId) {
        return locationPersistence.list(clientId);
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
        if (location == null || location.getId() == null || location.getVersion() == null) {
            throw new IllegalArgumentException("Location Id and Version cannot be null.");
        }
        Location dbLocation = locationPersistence.reload(location);
        if (dbLocation != null) {
            location.setBelongsTo(dbLocation.getBelongsTo());
            location.setUsingThisLocation(dbLocation.getUsingThisLocation());
        }
        return locationPersistence.save(location);
    }

    @Override
    @Transactional
    public void updateClientLocations(Client toUpdate, ClientLocationModificationRequest modificationRequest) {
        if (modificationRequest == null || toUpdate == null || toUpdate.getId() == null) {
            return;
        }
        Client dbClient = clientPersistence.reload(toUpdate.getId());
        if (dbClient != null) {
            if (!CollectionUtils.isEmpty(modificationRequest.getAdded())) {
                for (Location location : modificationRequest.getAdded()) {
                    Location dbLocation = locationPersistence.reload(location);
                    if (dbLocation != null) {
                        dbLocation.addClientUsingThisLocation(dbClient);
                        locationPersistence.save(dbLocation);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(modificationRequest.getDeleted())) {
                for (Location location : modificationRequest.getDeleted()) {
                    Location dbLocation = locationPersistence.reload(location);
                    dbLocation.getUsingThisLocation().remove(dbClient);
                }
            }
        }
    }
}
