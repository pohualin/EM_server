package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.service.ClientLocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Set;

/**
 * Spring JPA ClientLocation Service Implementation
 */
@Service
public class ClientLocationServiceImpl implements ClientLocationService {

    @Resource
    ClientLocationPersistence clientLocationPersistence;

    @Override
    public Page<ClientLocation> find(Client client, Pageable pageable) {
        return clientLocationPersistence.find(client, pageable);
    }

    @Override
    @Transactional
    public void remove(ClientLocation toRemove) {
        clientLocationPersistence.remove(toRemove);
    }

    @Override
    @Transactional
    public Page<ClientLocation> create(Client client, Set<Location> locations, Pageable pageable) {
        for (Location location : locations) {
            clientLocationPersistence.create(location, client);
        }
        return find(client, pageable);
    }
}
