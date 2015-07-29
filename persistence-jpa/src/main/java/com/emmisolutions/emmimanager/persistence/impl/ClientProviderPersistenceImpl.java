package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.persistence.ClientProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientProviderRepository;
import com.emmisolutions.emmimanager.persistence.repo.ClientRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProviderRepository;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA/Spring Data implementation
 */
@Repository
public class ClientProviderPersistenceImpl implements ClientProviderPersistence {

    @Resource
    ClientProviderRepository clientProviderRepository;

    @Resource
    ClientRepository clientRepository;

    @Resource
    ProviderRepository providerRepository;

    @Override
    public Page<ClientProvider> findByClientId(Long clientId, Pageable pageable) {
        if (clientId == null) {
            throw new InvalidDataAccessApiUsageException("Client Id cannot be null");
        }
        return clientProviderRepository.findByClientId(clientId,
                pageable != null ? pageable : new PageRequest(0, 10, Sort.Direction.DESC, "createdDate"));
    }
    
	@Override
	public Page<ClientProvider> findByProviderId(Long providerId,
			Pageable pageable) {
		if (providerId == null) {
			throw new InvalidDataAccessApiUsageException(
					"Provider Id cannot be null");
		}
		return clientProviderRepository.findByProviderId(providerId,
				pageable != null ? pageable : new PageRequest(0, 10,
						Sort.Direction.DESC, "createdDate"));
	}

    @Override
    public ClientProvider create(Long providerId, Long clientId) {
        ClientProvider existing = reload(providerId, clientId);
        return existing != null ? existing :
                clientProviderRepository.save(
                        new ClientProvider(clientRepository.findOne(clientId), providerRepository.findOne(providerId)));
    }

    @Override
    public void remove(Long id) {
        clientProviderRepository.delete(id);
    }

    @Override
    public ClientProvider reload(Long clientProviderId) {
        return (clientProviderId != null) ? clientProviderRepository.findOne(clientProviderId) : null;
    }

    @Override
    public List<ClientProvider> load(Long clientId, Page<Provider> matchedProviders) {
        if (clientId == null || matchedProviders == null || CollectionUtils.isEmpty(matchedProviders.getContent())) {
            return new ArrayList<>();
        }
        return clientProviderRepository.findByClientIdAndProviderIn(clientId, matchedProviders.getContent());
    }

    @Override
    public ClientProvider reload(Long providerId, Long clientId) {
        return clientProviderRepository.findByClientIdAndProviderId(clientId, providerId);
    }

    @Override
    public ClientProvider save(ClientProvider clientProvider) {
        return clientProviderRepository.save(clientProvider);
    }
    
    @Override
    public ClientProvider findByClientIdProviderId(Long clientId, Long providerId){
    	return clientProviderRepository.findByClientIdAndProviderId(clientId, providerId);
    }

}
