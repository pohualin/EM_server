package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientLocationRepository;
import com.emmisolutions.emmimanager.persistence.repo.ClientRepository;
import com.emmisolutions.emmimanager.persistence.repo.LocationRepository;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA/Spring Data implementation
 */
@Repository
public class ClientLocationPersistenceImpl implements ClientLocationPersistence {

    @Resource
    ClientLocationRepository clientLocationRepository;

    @Resource
    ClientRepository clientRepository;

    @Resource
    LocationRepository locationRepository;

    @Override
    public Page<ClientLocation> find(Long clientId, Pageable pageable) {
        if (clientId == null) {
            throw new InvalidDataAccessApiUsageException("Client Id cannot be null");
        }
        return clientLocationRepository.findByClientId(clientId,
                pageable != null ? pageable : new PageRequest(0, 10, Sort.Direction.DESC, "createdDate"));
    }

    @Override
    public ClientLocation create(Long locationId, Long clientId) {
        ClientLocation existing = reload(locationId, clientId);
        return existing != null ? existing :
                clientLocationRepository.save(
                        new ClientLocation(clientRepository.findOne(clientId), locationRepository.findOne(locationId)));
    }

    @Override
    public void remove(Long id) {
        clientLocationRepository.delete(id);
    }

    @Override
    public ClientLocation reload(Long clientLocationId) {
        return (clientLocationId != null) ? clientLocationRepository.findOne(clientLocationId) : null;
    }

    @Override
    public List<ClientLocation> load(Long clientId, Page<Location> matchedLocations) {
        if (clientId == null || matchedLocations == null || CollectionUtils.isEmpty(matchedLocations.getContent())) {
            return new ArrayList<>();
        }
        return clientLocationRepository.findByClientIdAndLocationIn(clientId, matchedLocations.getContent());
    }

    @Override
    public ClientLocation reload(Long locationId, Long clientId) {
        return clientLocationRepository.findByClientIdAndLocationId(clientId, locationId);
    }


}
