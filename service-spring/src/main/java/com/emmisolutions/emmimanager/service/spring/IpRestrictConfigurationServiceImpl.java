package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.persistence.IpRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.IpRestrictConfigurationService;

/**
 * Service implementation class for IpRestrictConfiguration entity
 *
 */
@Service
public class IpRestrictConfigurationServiceImpl implements
        IpRestrictConfigurationService {

    @Resource
    ClientService clientService;

    @Resource
    IpRestrictConfigurationPersistence ipRestrictConfigurationPersistence;

    @Override
    @Transactional
    public IpRestrictConfiguration create(
            IpRestrictConfiguration ipRestrictConfiguration) {
        Client reload = clientService.reload(ipRestrictConfiguration
                .getClient());
        ipRestrictConfiguration.setClient(reload);
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
    public Page<IpRestrictConfiguration> getByClient(Pageable pageable,
            Client client) {
        if (client == null || client.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "Client or clientId can not be null.");
        }
        return ipRestrictConfigurationPersistence
                .list(pageable, client.getId());
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
        Client reloadClientConfig = clientService
                .reload(ipRestrictConfiguration.getClient());
        ipRestrictConfiguration.setClient(reloadClientConfig);
        return ipRestrictConfigurationPersistence
                .saveOrUpdate(ipRestrictConfiguration);
    }

}
