package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.service.ClientLocationService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Spring JPA ClientLocation Service Implementation
 */
@Service
public class ClientLocationServiceImpl implements ClientLocationService {

    @Resource
    ClientLocationPersistence clientLocationPersistence;

    @Resource
    LocationPersistence locationPersistence;

    @Override
    public Page<ClientLocation> find(Client client, Pageable pageable) {
        if (client == null){
            throw new InvalidDataAccessApiUsageException("Client cannot be null");
        }
        return clientLocationPersistence.find(client.getId(), pageable);
    }

    @Override
    @Transactional
    public void remove(ClientLocation clientLocation) {
        if (clientLocation == null){
            throw new InvalidDataAccessApiUsageException("ClientLocation cannot be null");
        }
        clientLocationPersistence.remove(clientLocation.getId());
    }

    @Override
    @Transactional
    public Set<ClientLocation> create(Client client, Set<Location> locations) {
        if (client == null) {
            throw new InvalidDataAccessApiUsageException("Client cannot be null");
        }
        Set<ClientLocation> ret = new HashSet<>();
        if (locations != null) {
            for (Location location : locations) {
                ret.add(clientLocationPersistence.create(location.getId(), client.getId()));
            }
        }
        return ret;
    }

    @Override
    public ClientLocation reload(ClientLocation clientLocation) {
        if (clientLocation == null){
            throw new InvalidDataAccessApiUsageException("ClientLocation cannot be null");
        }
        return clientLocationPersistence.reload(clientLocation.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientLocation> findPossibleLocationsToAdd(Client client, LocationSearchFilter locationSearchFilter, Pageable pageable) {
        if (client == null) {
            throw new InvalidDataAccessApiUsageException("Client cannot be null");
        }

        // find matching locations
        Page<Location> matchedLocations = locationPersistence.list(pageable, locationSearchFilter);

        // find ClientLocations for the page of matching locations
        Map<Location, ClientLocation> matchedClientLocationMap = new HashMap<>();
        for (ClientLocation matchedClientLocation : clientLocationPersistence.load(client.getId(), matchedLocations)) {
            matchedClientLocation.setClient(client);
            matchedClientLocationMap.put(matchedClientLocation.getLocation(), matchedClientLocation);
        }

        // make ClientLocation objects from matched locations (from search) and the existing client locations
        List<ClientLocation> clientLocations = new ArrayList<>();
        for (Location matchingLocation : matchedLocations) {
            ClientLocation alreadyAssociated = matchedClientLocationMap.get(matchingLocation);
            clientLocations.add(alreadyAssociated != null ? alreadyAssociated : new ClientLocation(null, matchingLocation));
        }
        return new PageImpl<>(clientLocations, pageable, matchedLocations.getTotalElements());
    }
}
