package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientContentSubscriptionConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientContentSubscriptionConfigurationRepository;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Persistence implementation for ClientContentSubscriptionConfiguration entity.
 */
@Repository
public class ClientContentSubscriptionConfigurationPersistenceImpl implements
				ClientContentSubscriptionConfigurationPersistence {

    @Resource
    ClientContentSubscriptionConfigurationRepository clientContentSubscriptionConfigurationRepository;
    
    @Override
	public Page<ClientContentSubscriptionConfiguration> findByClient(Long clientId, Pageable pageable){
    	if (clientId == null) {
            throw new InvalidDataAccessApiUsageException("Client Id cannot be null");
        }
        return clientContentSubscriptionConfigurationRepository.findByClientId(clientId,
                pageable != null ? pageable : new PageRequest(0, 20, Sort.Direction.DESC, "id"));
    }

	@Override
	public ClientContentSubscriptionConfiguration saveOrUpdate(
			ClientContentSubscriptionConfiguration clientContentSubscritpionConfiguration) {
		return clientContentSubscriptionConfigurationRepository.save(clientContentSubscritpionConfiguration);
	}

	@Override
	public ClientContentSubscriptionConfiguration reload(Long id) {
		 if (id == null) {
	            return null;
	    }
		return clientContentSubscriptionConfigurationRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		clientContentSubscriptionConfigurationRepository.delete(id);	
	}
}
