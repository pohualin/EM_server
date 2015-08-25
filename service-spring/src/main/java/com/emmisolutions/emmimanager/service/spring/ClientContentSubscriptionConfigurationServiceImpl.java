package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.persistence.ClientContentSubscriptionConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.ContentSubscriptionPersistence;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.service.ClientContentSubscriptionConfigurationService;
import com.emmisolutions.emmimanager.service.ClientService;

/**
 * Service layer for ClientContentSubscriptionConfiguration
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
    	if(clientContentSubscriptionConfiguration.getClient().getId() == null){
    		throw new InvalidDataAccessApiUsageException(
                    "ClientId can not be null for creating the client's content subscription.");
    	}
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
    	
    	ClientContentSubscriptionConfiguration reloadContentConfiguration = clientContentSubscriptionConfigurationPersistence
        	.reload(clientContentSubscriptionConfiguration.getId());
         if (reloadContentConfiguration != null) {
        	 clientContentSubscriptionConfigurationPersistence.delete(clientContentSubscriptionConfiguration
                     .getId());
         }
    }

	@Override
	public Page<ClientContentSubscriptionConfiguration> findByClient(
			Client client, Pageable pageable) {
		 Client toUse = clientService.reload(client);
	     if (toUse != null) {
	        	return clientContentSubscriptionConfigurationPersistence.findByClient(client.getId(), pageable);
	     }else{
	            return null;
	     }
  	}

	@Override
	public ClientContentSubscriptionConfiguration reload(
			ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration) {
		
		ClientContentSubscriptionConfiguration reloadContentConfiguration = clientContentSubscriptionConfigurationPersistence
	        	.reload(clientContentSubscriptionConfiguration.getId());
		if (reloadContentConfiguration != null) {
			return clientContentSubscriptionConfigurationPersistence
	                .reload(clientContentSubscriptionConfiguration.getId());
        }else{
        	return null;
        }
 
	}
}
