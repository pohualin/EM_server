package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Persistence API for ClientProvider objects
 */
public interface ClientProviderPersistence {

    /**
     * Find a page of client providers for a client
     *
     * @param clientId to find client providers
     * @param pageable which page to fetch
     * @return a page of client provider objects
     */
    Page<ClientProvider> findByClientId(Long clientId, Pageable pageable);

    /**
     * Find a page of client providers for a client
     *
     * @param providerId to find client providers
     * @param pageable   which page to fetch
     * @return a page of client provider objects
     */
    Page<ClientProvider> findByProviderId(Long providerId, Pageable pageable);

    /**
     * Creates a ClientProvider from a Provider and a Client
     *
     * @param providerId to use
     * @param clientId   to use
     * @return saved ClientProvider
     */
    ClientProvider create(Long providerId, Long clientId);

    /**
     * Removes a ClientProvider from the db
     *
     * @param id to delete
     */
    void remove(Long id);

    /**
     * Loads a ClientProvider from the database
     *
     * @param clientProviderId to load
     * @return a ClientProvider or {@literal null}
     */
    ClientProvider reload(Long clientProviderId);

    /**
     * Load matching ClientProvider objects where the Provider is within one of the
     * matched providers
     *
     * @param clientId         to use
     * @param matchedProviders to filter by
     * @return the List of ClientProviders matching both the client id and page of providers, never null
     */
    List<ClientProvider> load(Long clientId, Page<Provider> matchedProviders);

    /**
     * Loads a ClientProvider from a Provider and a Client
     *
     * @param providerId to use
     * @param clientId   to use
     * @return saved ClientProvider or null
     */
    ClientProvider reload(Long providerId, Long clientId);

    /**
     * Saves a client provider
     *
     * @param clientProvider to save
     * @return saved ClientProvider
     */
    ClientProvider save(ClientProvider clientProvider);

    /**
     * Find a client provider
     *
     * @param clientId    to use
     * @param providerId to use
     * @return found ClientProvider
     */
    ClientProvider findByClientIdProviderId(Long clientId, Long providerId);
}
