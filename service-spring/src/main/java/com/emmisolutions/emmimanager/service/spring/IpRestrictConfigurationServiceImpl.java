package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.IpRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.IpRestrictConfigurationService;

/**
 * Service implementation class for IpRestrictConfiguration entity
 *
 */
@Service
public class IpRestrictConfigurationServiceImpl implements
        IpRestrictConfigurationService {

    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;

    @Resource
    IpRestrictConfigurationPersistence ipRestrictConfigurationPersistence;

    @Override
    @Transactional
    public IpRestrictConfiguration create(
            IpRestrictConfiguration ipRestrictConfiguration) {
        ClientRestrictConfiguration reload = clientRestrictConfigurationService
                .reload(ipRestrictConfiguration
                        .getClientRestrictConfiguration());
        ipRestrictConfiguration.setClientRestrictConfiguration(reload);
        return ipRestrictConfigurationPersistence
                .saveOrUpdate(ipRestrictConfiguration);
    }

    @Override
    @Transactional
    public void delete(IpRestrictConfiguration ipRestrictConfiguration) {
        if (ipRestrictConfiguration == null
                || ipRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "IpRestrictConfiguration or ipRestrictConfigurationId can not be null.");
        }
        ipRestrictConfigurationPersistence.delete(ipRestrictConfiguration
                .getId());
    }

    @Override
    @Transactional
    public Page<IpRestrictConfiguration> getByClientRestrictConfiguration(
            Pageable pageable,
            ClientRestrictConfiguration clientRestrictConfiguration) {
        if (clientRestrictConfiguration == null
                || clientRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientRestrictConfiguration or clientRestrictConfigurationId can not be null.");
        }
        return ipRestrictConfigurationPersistence.list(pageable,
                clientRestrictConfiguration.getId());
    }

    @Override
    @Transactional
    public IpRestrictConfiguration reload(
            IpRestrictConfiguration ipRestrictConfiguration) {
        if (ipRestrictConfiguration == null
                || ipRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "IpRestrictConfiguration or ipRestrictConfigurationId can not be null.");
        }
        return ipRestrictConfigurationPersistence
                .reload(ipRestrictConfiguration.getId());
    }

    @Override
    @Transactional
    public IpRestrictConfiguration update(
            IpRestrictConfiguration ipRestrictConfiguration) {
        if (ipRestrictConfiguration == null
                || ipRestrictConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "IpRestrictConfiguration or ipRestrictConfigurationId can not be null.");
        }
        ClientRestrictConfiguration reloadClientConfig = clientRestrictConfigurationService
                .reload(ipRestrictConfiguration
                        .getClientRestrictConfiguration());
        ipRestrictConfiguration
                .setClientRestrictConfiguration(reloadClientConfig);
        return ipRestrictConfigurationPersistence
                .saveOrUpdate(ipRestrictConfiguration);
    }

}
