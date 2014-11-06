package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.persistence.ClientProviderPersistence;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.service.ClientProviderService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ProviderService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Spring JPA ClientProvider Service Implementation
 */
@Service
public class ClientProviderServiceImpl implements ClientProviderService {

    @Resource
    ClientProviderPersistence clientProviderPersistence;

    @Resource
    ProviderPersistence providerPersistence;

    @Resource
    ProviderService providerService;

    @Resource
    ClientService clientService;

    @Override
    public Page<ClientProvider> find(Client client, Pageable pageable) {
        if (client == null){
            throw new InvalidDataAccessApiUsageException("Client cannot be null");
        }
        return clientProviderPersistence.find(client.getId(), pageable);
    }

    @Override
    @Transactional
    public void remove(ClientProvider clientProvider) {
        if (clientProvider == null){
            throw new InvalidDataAccessApiUsageException("ClientProvider cannot be null");
        }
        clientProviderPersistence.remove(clientProvider.getId());
    }

    @Override
    @Transactional
    public Set<ClientProvider> create(Client client, Set<Provider> providers) {
        if (client == null) {
            throw new InvalidDataAccessApiUsageException("Client cannot be null");
        }
        Set<ClientProvider> ret = new HashSet<>();
        if (providers != null) {
            for (Provider provider : providers) {
                if (provider != null) {
                    ret.add(clientProviderPersistence.create(provider.getId(), client.getId()));
                }
            }
        }
        return ret;
    }

    @Override
    @Transactional
    public ClientProvider create(ClientProvider clientProvider) {
        if (clientProvider == null) {
            throw new InvalidDataAccessApiUsageException("ClientProvider cannot be null");
        }
        // create a new provider
        clientProvider.setId(null);
        clientProvider.setVersion(null);
        boolean active = clientProvider.getProvider().isActive();
        Provider created = providerService.create(clientProvider.getProvider());
        created.setActive(active);
        clientProvider.setProvider(created);
        clientProvider.setClient(clientService.reload(clientProvider.getClient()));
        return clientProviderPersistence.save(clientProvider);
    }

    @Override
    public ClientProvider reload(ClientProvider clientProvider) {
        if (clientProvider == null){
            throw new InvalidDataAccessApiUsageException("ClientProvider cannot be null");
        }
        return clientProviderPersistence.reload(clientProvider.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientProvider> findPossibleProvidersToAdd(Client client, ProviderSearchFilter providerSearchFilter, Pageable pageable) {
        if (client == null) {
            throw new InvalidDataAccessApiUsageException("Client cannot be null");
        }

        // find matching providers
        Page<Provider> matchedProviders = providerPersistence.list(pageable, providerSearchFilter);

        // find ClientProviders for the page of matching providers
        Map<Provider, ClientProvider> matchedClientProviderMap = new HashMap<>();
        for (ClientProvider matchedClientProvider : clientProviderPersistence.load(client.getId(), matchedProviders)) {
            matchedClientProvider.setClient(client);
            matchedClientProviderMap.put(matchedClientProvider.getProvider(), matchedClientProvider);
        }

        // make ClientProvider objects from matched providers (from search) and the existing client providers
        List<ClientProvider> clientProviders = new ArrayList<>();
        for (Provider matchingProvider : matchedProviders) {
            ClientProvider alreadyAssociated = matchedClientProviderMap.get(matchingProvider);
            clientProviders.add(alreadyAssociated != null ? alreadyAssociated : new ClientProvider(null, matchingProvider));
        }
        return new PageImpl<>(clientProviders, pageable, matchedProviders.getTotalElements());
    }

    @Override
    @Transactional
    public ClientProvider update(ClientProvider clientProvider) {
        providerService.update(clientProvider.getProvider());
        return clientProviderPersistence.save(clientProvider);
    }
}
