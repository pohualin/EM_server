package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.ClientService;

/**
 * Service Implementation class to deal with ClientRestrictConfiguration
 *
 */
@Service
public class ClientRestrictConfigurationServiceImpl implements
        ClientRestrictConfigurationService {

    @Resource
    ClientService clientService;

    @Resource
    ClientRestrictConfigurationPersistence clientRestrictConfigurationPersistence;

    @Override
    public ClientRestrictConfiguration create(
            ClientRestrictConfiguration clientRestrictConfiguration) {
        Client client = clientService.reload(clientRestrictConfiguration
                .getClient());
        clientRestrictConfiguration.setClient(client);
        return clientRestrictConfigurationPersistence
                .saveOrUpdate(clientRestrictConfiguration);
    }

    @Override
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
    public ClientRestrictConfiguration getByClient(Client client) {
        Client reloadClient = clientService.reload(client);
        return clientRestrictConfigurationPersistence
                .findByClient(reloadClient);
    }

    @Override
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
