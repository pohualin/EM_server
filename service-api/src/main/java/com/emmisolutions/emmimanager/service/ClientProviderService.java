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
    Page<ClientProvider> find(Client client, Pageable pageable);

    /**
     * Removes a ClientProvider
     *
     * @param clientProvider the ClientProvider to delete
     */
    void remove(ClientProvider clientProvider);

    /**
     * Create providers for the client.
     *
     * @param client    for which to create them
     * @param providers to associate
     * @return the set of saved providers
     */
    Set<ClientProvider> create(Client client, Set<Provider> providers);

    /**
     * Creates a brand new provider as well as a new client provider
     *
     * @param client   to use
     * @param provider to create/save and associate to the client
     * @return saved ClientProvider
     */
    ClientProvider createProviderAndAssociateTo(Client client, Provider provider);

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
}
