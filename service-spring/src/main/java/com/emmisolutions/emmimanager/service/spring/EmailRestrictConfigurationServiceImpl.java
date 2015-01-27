package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.EmailRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.EmailRestrictConfigurationService;

/**
 * Service implementation class for EmailRestrictConfiguration entity
 *
 */
@Service
public class EmailRestrictConfigurationServiceImpl implements
        EmailRestrictConfigurationService {

    @Resource
    ClientService clientService;

    @Resource
    EmailRestrictConfigurationPersistence emailRestrictConfigurationPersistence;

    @Override
    @Transactional
    public EmailRestrictConfiguration create(
            EmailRestrictConfiguration emailRestrictConfiguration) {
        Client reload = clientService.reload(emailRestrictConfiguration
                .getClient());
        emailRestrictConfiguration.setClient(reload);
        return emailRestrictConfigurationPersistence
                .saveOrUpdate(emailRestrictConfiguration);
    }

    @Override
    @Transactional
    public void delete(EmailRestrictConfiguration emailRestrictConfiguration) {
        if (emailRestrictConfiguration == null
                || emailRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "EmailRestrictConfiguration or emailRestrictConfigurationId can not be null.");
        }
        emailRestrictConfigurationPersistence.delete(emailRestrictConfiguration
                .getId());
    }

    @Override
    @Transactional
    public Page<EmailRestrictConfiguration> getByClient(Pageable pageable,
            Client client) {
        if (client == null || client.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "Client or clientId can not be null.");
        }
        return emailRestrictConfigurationPersistence.list(pageable,
                client.getId());
    }

    @Override
    @Transactional
    public EmailRestrictConfiguration reload(
            EmailRestrictConfiguration emailRestrictConfiguration) {
        if (emailRestrictConfiguration == null
                || emailRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "EmailRestrictConfiguration or emailRestrictConfigurationId can not be null.");
        }
        return emailRestrictConfigurationPersistence
                .reload(emailRestrictConfiguration.getId());
    }

    @Override
    @Transactional
    public EmailRestrictConfiguration update(
            EmailRestrictConfiguration emailRestrictConfiguration) {
        if (emailRestrictConfiguration == null
                || emailRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "EmailRestrictConfiguration or emailRestrictConfigurationId can not be null.");
        }
        Client reloadClient = clientService.reload(emailRestrictConfiguration
                .getClient());
        emailRestrictConfiguration.setClient(reloadClient);
        return emailRestrictConfigurationPersistence
                .saveOrUpdate(emailRestrictConfiguration);
    }
}
