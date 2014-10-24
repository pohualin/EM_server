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
    public Page<ClientLocation> find(Long clientId, Pageable pageable) {
        return clientLocationPersistence.find(clientId, pageable);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        clientLocationPersistence.remove(id);
    }

    @Override
    @Transactional
    public Set<ClientLocation> create(Long clientId, Set<Location> locations) {
        if (clientId == null) {
            throw new InvalidDataAccessApiUsageException("Client and locations cannot be null");
        }
        Set<ClientLocation> ret = new HashSet<>();
        if (locations != null) {
            for (Location location : locations) {
                ret.add(clientLocationPersistence.create(location.getId(), clientId));
            }
        }
        return ret;
    }

    @Override
    public ClientLocation reload(Long clientLocationId) {
        return clientLocationPersistence.reload(clientLocationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientLocation> findPossibleLocationsToAdd(Long clientId, LocationSearchFilter locationSearchFilter, Pageable pageable) {

        // find matching locations
        Page<Location> matchedLocations = locationPersistence.list(pageable, locationSearchFilter);

        // find ClientLocations for the page of matching locations
        Map<Location, ClientLocation> matchedClientLocationMap = new HashMap<>();
        Client sparseClient = new Client(clientId);
        for (ClientLocation matchedClientLocation : clientLocationPersistence.load(clientId, matchedLocations)) {
            matchedClientLocation.setClient(sparseClient);
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
