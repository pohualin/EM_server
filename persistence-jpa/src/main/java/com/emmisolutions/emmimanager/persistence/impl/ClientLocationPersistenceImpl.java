package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientLocationRepository;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * JPA/Spring Data implementation
 */
@Repository
public class ClientLocationPersistenceImpl implements ClientLocationPersistence {

    @Resource
    ClientLocationRepository clientLocationRepository;

    @Override
    public Page<ClientLocation> find(Client client, Pageable pageable) {
        if (client == null || client.getId() == null){
            throw new InvalidDataAccessApiUsageException("Client cannot be null");
        }
        return clientLocationRepository.findByClient(client,
                pageable != null ? pageable : new PageRequest(0, 10, Sort.Direction.DESC, "createdDate"));
    }

    @Override
    public ClientLocation create(Location location, Client client) {
        return clientLocationRepository.save(new ClientLocation(client, location));
    }

    @Override
    public void remove(ClientLocation toRemove) {
        clientLocationRepository.delete(toRemove);
    }
}
