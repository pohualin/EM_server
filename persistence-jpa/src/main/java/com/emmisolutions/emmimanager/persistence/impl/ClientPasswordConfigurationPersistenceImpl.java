package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientPasswordConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientPasswordConfigurationRepository;

/**
 * Persistence Implementation to deal with PasswordConfiguration
 */
@Repository
public class ClientPasswordConfigurationPersistenceImpl implements
        ClientPasswordConfigurationPersistence {

    @Resource
    ClientPasswordConfigurationRepository clientPasswordConfigurationRepository;

    @Override
    public ClientPasswordConfiguration findByClient(Client client) {
        return clientPasswordConfigurationRepository.findByClient(client);
    }

    @Override
    public ClientPasswordConfiguration reload(Long id) {
        return clientPasswordConfigurationRepository.findOne(id);
    }

    @Override
    public ClientPasswordConfiguration saveOrUpdate(
            ClientPasswordConfiguration clientPasswordConfiguration) {
        return clientPasswordConfigurationRepository
                .save(clientPasswordConfiguration);
    }

    @Override
    public void delete(Long id) {
        clientPasswordConfigurationRepository.delete(id);
    }

}
