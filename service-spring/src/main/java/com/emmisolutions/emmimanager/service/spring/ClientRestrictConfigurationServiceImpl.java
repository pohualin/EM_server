package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.ClientService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Service Implementation class to deal with ClientRestrictConfiguration
 */
@Service
public class ClientRestrictConfigurationServiceImpl implements
        ClientRestrictConfigurationService {

    @Resource
    ClientService clientService;

    @Resource
    ClientRestrictConfigurationPersistence clientRestrictConfigurationPersistence;

    @Override
    @Transactional
    public ClientRestrictConfiguration create(
            ClientRestrictConfiguration clientRestrictConfiguration) {
        Client client = clientService.reload(clientRestrictConfiguration
                .getClient());
        clientRestrictConfiguration.setClient(client);
        return clientRestrictConfigurationPersistence
                .saveOrUpdate(clientRestrictConfiguration);
    }

    @Override
    @Transactional
    public void delete(ClientRestrictConfiguration clientRestrictConfiguration) {
        if (clientRestrictConfiguration == null
                || clientRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientRestrictConfiguration or clientRestrictConfigurationId can not be null.");
        }
        clientRestrictConfigurationPersistence
                .delete(clientRestrictConfiguration.getId());
    }

    @Override
    @Transactional
    public ClientRestrictConfiguration getByClient(Client client) {
        return client != null && client.getId() != null ?
                clientRestrictConfigurationPersistence
                        .findByClient(client) : null;
    }

    @Override
    @Transactional
    public ClientRestrictConfiguration reload(
            ClientRestrictConfiguration clientRestrictConfiguration) {
        if (clientRestrictConfiguration == null
                || clientRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientRestrictConfiguration or clientRestrictConfigurationId can not be null.");
        }
        return clientRestrictConfigurationPersistence
                .reload(clientRestrictConfiguration.getId());
    }

    @Override
    @Transactional
    public ClientRestrictConfiguration update(
            ClientRestrictConfiguration clientRestrictConfiguration) {
        if (clientRestrictConfiguration == null
                || clientRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientRestrictConfiguration or clientRestrictConfigurationId can not be null.");
        }
        ClientRestrictConfiguration reload = this
                .reload(clientRestrictConfiguration);
        clientRestrictConfiguration.setClient(reload.getClient());
        return clientRestrictConfigurationPersistence
                .saveOrUpdate(clientRestrictConfiguration);
    }

}
