package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientRestrictConfigurationRepository;

/**
 * Persistence implementation for ClientRestrictConfiguration entity.
 */
@Repository
public class ClientRestrictConfigurationPersistenceImpl implements
        ClientRestrictConfigurationPersistence {

    @Resource
    ClientRestrictConfigurationRepository clientRestrictConfigurationRepository;

    @Override
    public void delete(Long id) {
        clientRestrictConfigurationRepository.delete(id);
    }

    @Override
    public ClientRestrictConfiguration findByClient(Client client) {
        return clientRestrictConfigurationRepository.findByClient(client);
    }

    @Override
    public ClientRestrictConfiguration reload(Long id) {
        return clientRestrictConfigurationRepository.findOne(id);
    }

    @Override
    public ClientRestrictConfiguration saveOrUpdate(
            ClientRestrictConfiguration clientRestrictConfiguration) {
        return clientRestrictConfigurationRepository
                .save(clientRestrictConfiguration);
    }

}
