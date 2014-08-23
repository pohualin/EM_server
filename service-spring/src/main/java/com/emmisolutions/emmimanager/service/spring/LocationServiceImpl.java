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
        // don't allow relationship setting
        location.setBelongsTo(null);
        location.setUsingThisLocation(null);
        return locationPersistence.save(location);
    }

    @Override
    @Transactional
    public Location update(Location location) {
        Location dbLocation = locationPersistence.reload(location);
        if (dbLocation == null) {
            throw new IllegalArgumentException("Attempting to update location which is not in database");
        }
        location.setBelongsTo(dbLocation.getBelongsTo());
        location.setUsingThisLocation(dbLocation.getUsingThisLocation());
        return locationPersistence.save(location);

    }

    /**
     * Updates the relationships to the client for the locations passed in this way:
     * <p/>
     * For the 'added' locations, the client will be added to the 'using this location' collection
     * for each of the locations in the list
     * <p/>
     * For the 'removed' locations, the client will be removed from the 'using this location' collection
     * for each of the locations in the list
     * <p/>
     * For the 'belongsTo' locations, the client will be set to the toRelateTo client when the location
     * has no current belongsTo in the db. If the location does have a current belongsTo, the relationship
     * can only ever be updated to 'null'. The reason for this is that the belongsTo relationship can only
     * be updated by the client that is already in the existing relationship. The idea is to allow a location
     * to belongTo only one client at a time, only when that client releases the relationship can it be
     * set again by a different client.
     *
     * @param toRelateTo          client to be used as the relationship
     * @param modificationRequest to update the locations
     */
    @Override
    @Transactional
    public void updateClientLocations(Client toRelateTo, ClientLocationModificationRequest modificationRequest) {
        if (modificationRequest == null || toRelateTo == null || toRelateTo.getId() == null) {
            return;
        }
        Client dbClient = clientPersistence.reload(toRelateTo.getId());
        if (dbClient != null) {
            if (!CollectionUtils.isEmpty(modificationRequest.getAdded())) {
                for (Location location : modificationRequest.getAdded()) {
                    Location dbLocation = locationPersistence.reload(location);
                    if (dbLocation != null) {
                        dbLocation.addClientUsingThisLocation(dbClient);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(modificationRequest.getDeleted())) {
                for (Location location : modificationRequest.getDeleted()) {
                    Location dbLocation = locationPersistence.reload(location);
                    if (dbLocation != null) {
                        dbLocation.getUsingThisLocation().remove(dbClient);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(modificationRequest.getBelongsToUpdated())) {
                for (Location location : modificationRequest.getBelongsToUpdated()) {
                    Location dbLocation = locationPersistence.reload(location);
                    if (dbLocation != null) {
                        if (dbLocation.getBelongsTo() == null) {
                            // wasn't set in db previously, set to passed client
                            dbLocation.setBelongsTo(dbClient);
                        } else if (dbLocation.getBelongsTo().equals(dbClient)) {
                            // was set already, allowed to null out relationship only
                            if (location.getBelongsTo() == null) {
                                dbLocation.setBelongsTo(null);
                            }
                        }
                    }
                }
            }
        }
    }
}
