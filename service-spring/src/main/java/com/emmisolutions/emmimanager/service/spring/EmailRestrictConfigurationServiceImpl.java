package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.EmailRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.EmailRestrictConfigurationService;

/**
 * Service implementation class for EmailRestrictConfiguration entity
 *
 */
@Service
public class EmailRestrictConfigurationServiceImpl implements
        EmailRestrictConfigurationService {

    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;

    @Resource
    EmailRestrictConfigurationPersistence emailRestrictConfigurationPersistence;

    @Override
    @Transactional
    public EmailRestrictConfiguration create(
            EmailRestrictConfiguration emailRestrictConfiguration) {
        ClientRestrictConfiguration reload = clientRestrictConfigurationService
                .reload(emailRestrictConfiguration
                        .getClientRestrictConfiguration());
        emailRestrictConfiguration.setClientRestrictConfiguration(reload);
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
    public Page<EmailRestrictConfiguration> getByClientRestrictConfiguration(
            Pageable pageable,
            ClientRestrictConfiguration clientRestrictConfiguration) {
        if (clientRestrictConfiguration == null
                || clientRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientRestrictConfiguration or clientRestrictConfigurationId can not be null.");
        }
        return emailRestrictConfigurationPersistence.list(pageable,
                clientRestrictConfiguration.getId());
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
        ClientRestrictConfiguration reloadClientConfig = clientRestrictConfigurationService
                .reload(emailRestrictConfiguration
                        .getClientRestrictConfiguration());
        emailRestrictConfiguration
                .setClientRestrictConfiguration(reloadClientConfig);
        return emailRestrictConfigurationPersistence
                .saveOrUpdate(emailRestrictConfiguration);
    }

}
