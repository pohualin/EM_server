package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.configuration.PasswordConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientPasswordConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultPasswordConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.PasswordConfigurationPersistence;
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

    @Resource
    PasswordConfigurationPersistence passwordConfigurationPersistence;

    @Override
    @Transactional
    public ClientPasswordConfiguration delete(
            ClientPasswordConfiguration clientPasswordConfiguration) {
        if (clientPasswordConfiguration == null
                || clientPasswordConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientPasswordConfiguration or clientPasswordConfigurationId cannot be null");
        }

        ClientPasswordConfiguration reload = clientPasswordConfigurationPersistence
                .reload(clientPasswordConfiguration.getId());
        ClientPasswordConfiguration configuration = new ClientPasswordConfiguration();
        configuration.setClient(reload.getClient());
        configuration.setDefaultPasswordConfiguration(reload
                .getDefaultPasswordConfiguration());
        clientPasswordConfigurationPersistence
                .delete(clientPasswordConfiguration.getId());
        return composePasswordConfiguration(configuration);
    }

    @Override
    @Transactional
    public ClientPasswordConfiguration get(Client client) {
        if (client == null || client.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "Client or clientId cannot be null");
        }

        Client toUse = clientService.reload(client);
        // Return null if client is not found
        if (toUse == null) {
            return null;
        }

        // Find ClientPasswordConfiguration by Client
        ClientPasswordConfiguration configuration = clientPasswordConfigurationPersistence
                .findByClient(toUse);

        // If nothing found, use system default
        if (configuration == null) {
            configuration = new ClientPasswordConfiguration();
            configuration.setClient(toUse);
            configuration
                    .setDefaultPasswordConfiguration(defaultPasswordConfigurationPersistence
                            .findSystemDefault());
        }

        return composePasswordConfiguration(configuration);
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
        ClientPasswordConfiguration reload = clientPasswordConfigurationPersistence
                .reload(clientPasswordConfiguration.getId());
        return composePasswordConfiguration(reload);
    }

    @Override
    @Transactional
    public ClientPasswordConfiguration save(
            ClientPasswordConfiguration clientPasswordConfiguration) {
        ClientPasswordConfiguration toSave = new ClientPasswordConfiguration();
        // Reload ClientPasswordConfiguration if there is an id
        if (clientPasswordConfiguration.getId() != null) {
            toSave = clientPasswordConfigurationPersistence
                    .reload(clientPasswordConfiguration.getId());
        } else {
            // Reload Client for ClientPasswordConfiguration
            if (clientPasswordConfiguration.getClient() == null
                    || clientPasswordConfiguration.getClient().getId() == null) {
                throw new InvalidDataAccessApiUsageException(
                        "Client or clientId cannot be null");
            } else {
                toSave.setClient(clientService
                        .reload(clientPasswordConfiguration.getClient()));
            }

            // Reload existing DefaultPasswordConfiguration for
            // ClientPasswordConfiguration
            if (clientPasswordConfiguration.getDefaultPasswordConfiguration() == null) {
                throw new InvalidDataAccessApiUsageException(
                        "DefaultPasswordConfiguration cannot be null");
            } else {
                toSave.setDefaultPasswordConfiguration(defaultPasswordConfigurationPersistence
                        .reload(clientPasswordConfiguration
                                .getDefaultPasswordConfiguration().getId()));
            }
        }

        // Save PasswordConfiguration for ClientPasswordConfiguration
        if (clientPasswordConfiguration.getPasswordConfiguration() != null) {
            toSave.setPasswordConfiguration(passwordConfigurationPersistence
                    .saveOrUpdate(clientPasswordConfiguration
                            .getPasswordConfiguration()));
            toSave = clientPasswordConfigurationPersistence
                    .saveOrUpdate(toSave);
        }

        return composePasswordConfiguration(toSave);
    }

    private ClientPasswordConfiguration composePasswordConfiguration(
            ClientPasswordConfiguration clientPasswordConfiguration) {
        ClientPasswordConfiguration passedBack = new ClientPasswordConfiguration();
        passedBack.setId(clientPasswordConfiguration.getId());
        passedBack.setClient(clientPasswordConfiguration.getClient());
        passedBack.setDefaultPasswordConfiguration(clientPasswordConfiguration
                .getDefaultPasswordConfiguration());
        passedBack.setPasswordConfiguration(clientPasswordConfiguration
                .getPasswordConfiguration());

        // Populate PasswordConfiguration from DefaultPasswordConfiguration if
        // PasswordConfiguration is not stored which means the client is using
        // default
        if (clientPasswordConfiguration.getPasswordConfiguration() == null) {
            PasswordConfiguration passwordConfiguration = new PasswordConfiguration();
            passedBack.setPasswordConfiguration(passwordConfiguration);
            passwordConfiguration.setName(clientPasswordConfiguration
                    .getDefaultPasswordConfiguration().getName());
            passwordConfiguration
                    .setPasswordExpirationDays(clientPasswordConfiguration
                            .getDefaultPasswordConfiguration()
                            .getDefaultPasswordExpirationDays());
            passwordConfiguration
                    .setDaysBetweenPasswordChange(clientPasswordConfiguration
                            .getDefaultPasswordConfiguration()
                            .getDefaultDaysBetweenPasswordChange());
            passwordConfiguration.setIdleTime(clientPasswordConfiguration
                    .getDefaultPasswordConfiguration().getDefaultIdleTime());
            passwordConfiguration.setLockoutAttemps(clientPasswordConfiguration
                    .getDefaultPasswordConfiguration()
                    .getDefaultLockoutAttemps());
            passwordConfiguration
                    .setLockoutReset(clientPasswordConfiguration
                            .getDefaultPasswordConfiguration()
                            .getDefaultLockoutReset());
            passwordConfiguration
                    .setLowercaseLetters(clientPasswordConfiguration
                            .getDefaultPasswordConfiguration()
                            .hasDefaultLowercaseLetters());
            passwordConfiguration.setNumbers(clientPasswordConfiguration
                    .getDefaultPasswordConfiguration().hasDefaultNumbers());
            passwordConfiguration.setPasswordLength(clientPasswordConfiguration
                    .getDefaultPasswordConfiguration()
                    .getDefaultPasswordLength());
            passwordConfiguration
                    .setPasswordExpirationDaysReminder(clientPasswordConfiguration
                            .getDefaultPasswordConfiguration()
                            .getDefaultPasswordExpirationDaysReminder());
            passwordConfiguration
                    .setSpecialChars(clientPasswordConfiguration
                            .getDefaultPasswordConfiguration()
                            .hasDefaultSpecialChars());
            passwordConfiguration
                    .setUppercaseLetters(clientPasswordConfiguration
                            .getDefaultPasswordConfiguration()
                            .hasDefaultUppercaseLetters());
            passwordConfiguration
                    .setPasswordRepetitions(clientPasswordConfiguration
                            .getDefaultPasswordConfiguration()
                            .getDefaultPasswordRepetitions());
        }
        return passedBack;
    }

}
