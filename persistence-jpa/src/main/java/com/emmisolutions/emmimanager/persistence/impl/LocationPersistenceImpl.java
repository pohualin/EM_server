package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

import static com.emmisolutions.emmimanager.persistence.impl.specification.LocationSpecifications.*;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Location Persistence API implementation
 */
@Repository
public class LocationPersistenceImpl implements LocationPersistence {

    @Resource
    LocationRepository locationRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Resource
    ClientPersistence clientPersistence;

    @Override
    @SuppressWarnings("unchecked")
    public Page<Location> list(Pageable page, LocationSearchFilter filter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        Client client = null;
        if (filter != null && filter.getClientId() != null){
            client = clientPersistence.reload(filter.getClientId());
        }
        return locationRepository.findAll(where(usedBy(client)).and(hasNames(filter)).and(isInStatus(filter)), page);
    }

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location reload(Location location) {
        if (location == null || location.getId() == null) {
            return null;
        }
        return locationRepository.findOne(location.getId());
    }

    @Override
    public Set<Long> list(Long clientId) {
        return new HashSet<>(locationRepository.findAllIdsByClientId(clientId));
    }

    @Override
    public Location reloadLocationUsedByClient(Client client, Long id) {
        if (client == null || client.getId() == null || id == null){
            return null;
        }
        return locationRepository.loadALocationUsedByClient(client.getId(), id);
    }
}
