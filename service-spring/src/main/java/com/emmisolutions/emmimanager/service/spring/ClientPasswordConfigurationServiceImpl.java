package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;
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
        validatePasswordConfiguration(clientPasswordConfiguration);
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

    private void validatePasswordConfiguration(
            ClientPasswordConfiguration clientPasswordConfiguration) {
        DefaultPasswordConfiguration defaultConfig = clientPasswordConfiguration
                .getDefaultPasswordConfiguration();

        if (clientPasswordConfiguration.getPasswordExpirationDays() < defaultConfig
                .getPasswordExpirationDaysMin()
                || clientPasswordConfiguration.getPasswordExpirationDays() > defaultConfig
                        .getPasswordExpirationDaysMax()) {
            throw new InvalidDataAccessApiUsageException("Password expiration days not in range.");
        }
        
        if (clientPasswordConfiguration.getDaysBetweenPasswordChange() < defaultConfig
                .getDaysBetweenPasswordChangeMin()
                || clientPasswordConfiguration.getDaysBetweenPasswordChange() > defaultConfig
                        .getDaysBetweenPasswordChangeMax()) {
            throw new InvalidDataAccessApiUsageException("Days between password change not in range.");
        }
        
        if (clientPasswordConfiguration.getIdleTime() < defaultConfig
                .getIdleTimeMin()
                || clientPasswordConfiguration.getIdleTime() > defaultConfig
                        .getIdleTimeMax()) {
            throw new InvalidDataAccessApiUsageException("Idle time not in range.");
        }
        
        if (clientPasswordConfiguration.getLockoutAttemps() < defaultConfig
                .getLockoutAttempsMin()
                || clientPasswordConfiguration.getLockoutAttemps() > defaultConfig
                        .getLockoutAttempsMax()) {
            throw new InvalidDataAccessApiUsageException("Lockout attemps not in range.");
        }
        
        if (clientPasswordConfiguration.getLockoutReset() < defaultConfig
                .getLockoutResetMin()
                || clientPasswordConfiguration.getLockoutReset() > defaultConfig
                        .getLockoutResetMax()) {
            throw new InvalidDataAccessApiUsageException("Lockout reset not in range.");
        }
        
        if (clientPasswordConfiguration.getPasswordLength() < defaultConfig
                .getPasswordLengthMin()
                || clientPasswordConfiguration.getPasswordLength() > defaultConfig
                        .getPasswordLengthMax()) {
            throw new InvalidDataAccessApiUsageException("Password length not in range.");
        }
        
        if (clientPasswordConfiguration.getPasswordExpirationDaysReminder() < defaultConfig
                .getPasswordExpirationDaysReminderMin()
                || clientPasswordConfiguration.getPasswordExpirationDaysReminder() > defaultConfig
                        .getPasswordExpirationDaysReminderMax()) {
            throw new InvalidDataAccessApiUsageException("Password expiration days reminder not in range.");
        }
        
        if (clientPasswordConfiguration.getPasswordRepetitions() < defaultConfig
                .getPasswordRepetitionsMin()
                || clientPasswordConfiguration.getPasswordRepetitions() > defaultConfig
                        .getPasswordRepetitionsMax()) {
            throw new InvalidDataAccessApiUsageException("Password repetitions not in range.");
        }

    }

}
