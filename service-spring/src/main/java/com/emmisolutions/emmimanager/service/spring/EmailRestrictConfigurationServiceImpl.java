package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.EmailRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.EmailRestrictConfigurationService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation class for EmailRestrictConfiguration entity
 *
 */
@Service
public class EmailRestrictConfigurationServiceImpl implements EmailRestrictConfigurationService {

    public static final String EXCEPTION_NON_NULL_CLIENT = "Client or clientId can not be null.";
    @Resource
    ClientService clientService;

    @Resource
    EmailRestrictConfigurationPersistence emailRestrictConfigurationPersistence;

    public static final String EXCEPTION_NON_NULL_EMAIL_RESTRICT_CONFIGURATION = "EmailRestrictConfiguration or emailRestrictConfigurationId can not be null.";

    public static final String EXCEPTION_NON_NULL_EMAIL_RESTRICT_CONFIGURATION_OR_CLIENT = "EmailRestrictConfiguration or Client can not be null.";

    @Override
    @Transactional
    public EmailRestrictConfiguration create(EmailRestrictConfiguration emailRestrictConfiguration) {
        if (emailRestrictConfiguration == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NON_NULL_EMAIL_RESTRICT_CONFIGURATION);
        }

        Client reload = clientService.reload(emailRestrictConfiguration.getClient());

        emailRestrictConfiguration.setClient(reload);

        return emailRestrictConfigurationPersistence.saveOrUpdate(emailRestrictConfiguration);
    }

    @Override
    @Transactional
    public void delete(EmailRestrictConfiguration emailRestrictConfiguration) {
        if (emailRestrictConfiguration == null || emailRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NON_NULL_EMAIL_RESTRICT_CONFIGURATION);
        }

        emailRestrictConfigurationPersistence.delete(emailRestrictConfiguration.getId());
    }

    @Override
    @Transactional
    public Page<EmailRestrictConfiguration> getByClient(Pageable pageable, Client client) {
        if (client == null || client.getId() == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NON_NULL_CLIENT);
        }

        return emailRestrictConfigurationPersistence.list(pageable, client.getId());
    }

    @Override
    @Transactional
    public EmailRestrictConfiguration reload(EmailRestrictConfiguration emailRestrictConfiguration) {
        if (emailRestrictConfiguration == null || emailRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NON_NULL_EMAIL_RESTRICT_CONFIGURATION);
        }

        return emailRestrictConfigurationPersistence.reload(emailRestrictConfiguration.getId());
    }

    @Override
    @Transactional
    public EmailRestrictConfiguration update(EmailRestrictConfiguration emailRestrictConfiguration) {
        if (emailRestrictConfiguration == null|| emailRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NON_NULL_EMAIL_RESTRICT_CONFIGURATION);
        }

        Client reloadClient = clientService.reload(emailRestrictConfiguration.getClient());
        emailRestrictConfiguration.setClient(reloadClient);

        return emailRestrictConfigurationPersistence.saveOrUpdate(emailRestrictConfiguration);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasDuplicateEmailEnding(EmailRestrictConfiguration emailRestrictConfiguration) {
        if (emailRestrictConfiguration == null || emailRestrictConfiguration.getClient() == null) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_NON_NULL_EMAIL_RESTRICT_CONFIGURATION_OR_CLIENT);
        }

        emailRestrictConfiguration.setClient(clientService.reload(emailRestrictConfiguration.getClient()));

        return emailRestrictConfigurationPersistence.findDuplicateEmailEnding(emailRestrictConfiguration) != null;
    }
}
