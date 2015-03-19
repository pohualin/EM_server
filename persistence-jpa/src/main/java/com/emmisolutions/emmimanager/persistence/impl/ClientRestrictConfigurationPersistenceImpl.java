package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientRestrictConfigurationRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Persistence implementation for ClientRestrictConfiguration entity.
 */
@Repository
public class ClientRestrictConfigurationPersistenceImpl implements
        ClientRestrictConfigurationPersistence {

    @Resource
    ClientRestrictConfigurationRepository clientRestrictConfigurationRepository;

    @Override
    @CacheEvict(value = "clientRestrictConfigurationByClient", allEntries = true)
    public void delete(Long id) {
        clientRestrictConfigurationRepository.delete(id);
    }

    @Override
    @Cacheable(value = "clientRestrictConfigurationByClient", key = "#p0.id")
    public ClientRestrictConfiguration findByClient(Client client) {
        return clientRestrictConfigurationRepository.findByClient(client);
    }

    @Override
    public ClientRestrictConfiguration reload(Long id) {
        return clientRestrictConfigurationRepository.findOne(id);
    }

    @Override
    @CacheEvict(value = "clientRestrictConfigurationByClient", key = "#p0.client.id")
    public ClientRestrictConfiguration saveOrUpdate(
            ClientRestrictConfiguration clientRestrictConfiguration) {
        return clientRestrictConfigurationRepository
                .save(clientRestrictConfiguration);
    }

}
