package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Service API for ClientProvider objects
 */
public interface ClientProviderService {

    /**
     * Find a page of client providers for a client
     *
     * @param client   to find client providers
     * @param pageable which page to fetch
     * @return a page of client provider objects
     */
    Page<ClientProvider> findByClient(Client client, Pageable pageable);

    /**
     * Find a page of client providers for a provider
     *
     * @param provider to find client providers
     * @param pageable which page to fetch
     * @return a page of client provider objects
     */
    Page<ClientProvider> findByProvider(Provider provider, Pageable pageable);

    /**
     * Removes a ClientProvider
     *
     * @param clientProvider the ClientProvider to delete
     */
    void remove(ClientProvider clientProvider);

    /**
     * Add existing providers to the existing client.
     *
     * @param client    for which to create them
     * @param providers existing persistent providers to associate to the client
     * @return the set of saved providers
     */
    Set<ClientProvider> create(Client client, Set<Provider> providers);

    /**
     * Cascades a create of a brand new provider as well as a new client provider
     *
     * @param clientProvider to create
     * @return saved ClientProvider
     */
    ClientProvider create(ClientProvider clientProvider);

    /**
     * Load a single client provider by id
     *
     * @param clientProvider the to reload
     * @return the ClientProvider or null
     */
    ClientProvider reload(ClientProvider clientProvider);

    /**
     * Finds a page of ClientProvider objects that are sparsely populated on the Client side
     * of the relationship.
     *
     * @param client               the client
     * @param pageable             a page
     * @param providerSearchFilter used to find the providers
     * @return a page of ClientProvider objects, the client relationship could be null
     */
    Page<ClientProvider> findPossibleProvidersToAdd(Client client, ProviderSearchFilter providerSearchFilter, Pageable pageable);

    /**
     * Updates a ClientProvider
     *
     * @param clientProvider to update
     * @return the updated ClientProvider
     */
    ClientProvider update(ClientProvider clientProvider);

    /**
     * Find a ClientProvider
     *
     * @param clientId   to use
     * @param providerId to use
     * @return clientProvider found
     */
    ClientProvider findByClientIdProviderId(Long clientId, Long providerId);

    /**
     * save a ClientProvider
     *
     * @param clientProvider to be saved
     * @return saved clientProvider
     */
    ClientProvider save(ClientProvider clientProvider);
}
