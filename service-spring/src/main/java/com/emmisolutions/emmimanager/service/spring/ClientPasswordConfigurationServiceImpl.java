package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientPasswordConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultPasswordConfigurationPersistence;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.ClientService;

/**
 * Service Implementation for ClientPasswordConfiguration
 */
@Service
public class ClientPasswordConfigurationServiceImpl implements
        ClientPasswordConfigurationService {

    @Resource
    ClientService clientService;

    @Resource
    ClientPasswordConfigurationPersistence clientPasswordConfigurationPersistence;

    @Resource
    DefaultPasswordConfigurationPersistence defaultPasswordConfigurationPersistence;

    @Override
    @Transactional
    public void delete(ClientPasswordConfiguration clientPasswordConfiguration) {
        if (clientPasswordConfiguration == null
                || clientPasswordConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientPasswordConfiguration or clientPasswordConfigurationId cannot be null");
        }
        clientPasswordConfigurationPersistence
                .delete(clientPasswordConfiguration.getId());
    }

    @Override
    @Transactional
    public ClientPasswordConfiguration get(Client client) {
        Client toUse = clientService.reload(client);

        // Find ClientPasswordConfiguration by Client
        ClientPasswordConfiguration configuration = clientPasswordConfigurationPersistence
                .findByClient(toUse);

        // If nothing found, use system default
        if (configuration == null) {
            configuration = new ClientPasswordConfiguration();
            configuration.setClient(toUse);
            configuration
                    .setDefaultPasswordConfiguration(defaultPasswordConfigurationPersistence
                            .findActive());
            composePasswordConfiguration(configuration);
        }

        return configuration;
    }

    @Override
    @Transactional
    public ClientPasswordConfiguration reload(
            ClientPasswordConfiguration clientPasswordConfiguration) {
        if (clientPasswordConfiguration == null
                || clientPasswordConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientPasswordConfiguration or clientPasswordConfigurationId can not be null");
        }
        return clientPasswordConfigurationPersistence
                .reload(clientPasswordConfiguration.getId());
    }

    @Override
    @Transactional
    public ClientPasswordConfiguration save(
            ClientPasswordConfiguration clientPasswordConfiguration) {
        clientPasswordConfiguration.setClient(clientService
                .reload(clientPasswordConfiguration.getClient()));
        clientPasswordConfiguration
                .setDefaultPasswordConfiguration(defaultPasswordConfigurationPersistence
                        .reload(clientPasswordConfiguration
                                .getDefaultPasswordConfiguration().getId()));
        return clientPasswordConfigurationPersistence
                .saveOrUpdate(clientPasswordConfiguration);
    }

    private void composePasswordConfiguration(
            ClientPasswordConfiguration clientPasswordConfiguration) {
        clientPasswordConfiguration.setName(clientPasswordConfiguration
                .getDefaultPasswordConfiguration().getName());
        clientPasswordConfiguration
                .setPasswordExpirationDays(clientPasswordConfiguration
                        .getDefaultPasswordConfiguration()
                        .getDefaultPasswordExpirationDays());
        clientPasswordConfiguration
                .setDaysBetweenPasswordChange(clientPasswordConfiguration
                        .getDefaultPasswordConfiguration()
                        .getDefaultDaysBetweenPasswordChange());
        clientPasswordConfiguration.setIdleTime(clientPasswordConfiguration
                .getDefaultPasswordConfiguration().getDefaultIdleTime());
        clientPasswordConfiguration
                .setLockoutAttemps(clientPasswordConfiguration
                        .getDefaultPasswordConfiguration()
                        .getDefaultLockoutAttemps());
        clientPasswordConfiguration.setLockoutReset(clientPasswordConfiguration
                .getDefaultPasswordConfiguration().getDefaultLockoutReset());
        clientPasswordConfiguration
                .setLowercaseLetters(clientPasswordConfiguration
                        .getDefaultPasswordConfiguration()
                        .hasDefaultLowercaseLetters());
        clientPasswordConfiguration.setNumbers(clientPasswordConfiguration
                .getDefaultPasswordConfiguration().hasDefaultNumbers());
        clientPasswordConfiguration
                .setPasswordLength(clientPasswordConfiguration
                        .getDefaultPasswordConfiguration()
                        .getDefaultPasswordLength());
        clientPasswordConfiguration
                .setPasswordExpirationDaysReminder(clientPasswordConfiguration
                        .getDefaultPasswordConfiguration()
                        .getDefaultPasswordExpirationDaysReminder());
        clientPasswordConfiguration.setSpecialChars(clientPasswordConfiguration
                .getDefaultPasswordConfiguration().hasDefaultSpecialChars());
        clientPasswordConfiguration
                .setUppercaseLetters(clientPasswordConfiguration
                        .getDefaultPasswordConfiguration()
                        .hasDefaultUppercaseLetters());
        clientPasswordConfiguration
                .setPasswordRepetitions(clientPasswordConfiguration
                        .getDefaultPasswordConfiguration()
                        .getDefaultPasswordRepetitions());
        clientPasswordConfiguration
                .setPasswordReset(clientPasswordConfiguration
                        .getDefaultPasswordConfiguration()
                        .isDefaultPasswordReset());
    }

}
