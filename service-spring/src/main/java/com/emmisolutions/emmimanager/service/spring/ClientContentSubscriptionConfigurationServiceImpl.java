package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.persistence.ClientContentSubscriptionConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.ClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.ContentSubscriptionPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.service.ClientContentSubscriptionConfigurationService;
import com.emmisolutions.emmimanager.service.ClientTeamSchedulingConfigurationService;
import com.emmisolutions.emmimanager.service.ClientService;

/**
 * Service layer for ClientTeamSchedulingConfiguration
 * 
 */
@Service
public class ClientContentSubscriptionConfigurationServiceImpl implements
				ClientContentSubscriptionConfigurationService {

    @Resource
    ClientService clientService;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    ClientContentSubscriptionConfigurationPersistence clientContentSubscriptionConfigurationPersistence;

    @Resource
    ContentSubscriptionPersistence contentSubscriptionConfigurationPersistence;
    
    @Override
    public Page<ContentSubscription> list(Pageable pageable) {
        return contentSubscriptionConfigurationPersistence.findActive(pageable);
    }

    @Override
    @Transactional
	public ClientContentSubscriptionConfiguration update(
			ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration) {
		if (clientContentSubscriptionConfiguration == null
                || clientContentSubscriptionConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientContentSubscriptionConfiguration or clientContentSubscriptionConfigurationId can not be null.");
        }
        Client reloadClient = clientService.reload(clientContentSubscriptionConfiguration
                .getClient());
        clientContentSubscriptionConfiguration.setClient(reloadClient);
        return clientContentSubscriptionConfigurationPersistence
                .saveOrUpdate(clientContentSubscriptionConfiguration);
	}

    @Override
    @Transactional
	public ClientContentSubscriptionConfiguration create(
			ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration) {
		Client reload = clientService.reload(clientContentSubscriptionConfiguration
                .getClient());
		clientContentSubscriptionConfiguration.setClient(reload);
        return clientContentSubscriptionConfigurationPersistence
                .saveOrUpdate(clientContentSubscriptionConfiguration);
	}

    @Override
    @Transactional
	public void delete(
			ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration) {
		if (clientContentSubscriptionConfiguration == null
                || clientContentSubscriptionConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientContentSubscriptionConfiguration or clientContentSubscriptionConfigurationId can not be null.");
        }
		clientContentSubscriptionConfigurationPersistence.delete(clientContentSubscriptionConfiguration
                .getId());
		
	}

	@Override
	public Page<ClientContentSubscriptionConfiguration> findByClient(
			Client client, Pageable pageable) {
		if (client == null || client.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "Client or clientId can not be null.");
        }
        return clientContentSubscriptionConfigurationPersistence.findByClient(client.getId(), pageable
                );
	}

	@Override
	public ClientContentSubscriptionConfiguration reload(
			ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration) {
		if (clientContentSubscriptionConfiguration == null
                || clientContentSubscriptionConfiguration.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientContentSubscriptionConfiguration or clientContentSubscriptionConfigurationId can not be null.");
        }
        return clientContentSubscriptionConfigurationPersistence
                .reload(clientContentSubscriptionConfiguration.getId());
	}

}
