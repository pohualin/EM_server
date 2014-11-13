package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data repo for ClientProvider objects
 */
public interface ClientProviderRepository extends JpaRepository<ClientProvider, Long>, JpaSpecificationExecutor<ClientProvider> {

    /**
     * Finds a page of providers by client id
     *
     * @param clientId to narrow by
     * @param page     the page specification
     * @return a page of matching ClientProvider objects
     */
    Page<ClientProvider> findByClientId(Long clientId, Pageable page);
    
    /**
     * Finds a page of providers by provider id
     *
     * @param providerId to narrow by
     * @param page     the page specification
     * @return a page of matching ClientProvider objects
     */
    Page<ClientProvider> findByProviderId(Long providerId, Pageable page);

    /**
     * Finds a full list of ClientProvider objects for a single client for a
     * discrete list of providers
     *
     * @param clientId  to narrow by
     * @param providers the discrete list of providers we are interested in
     * @return a List of ClientProvider objects
     */
    List<ClientProvider> findByClientIdAndProviderIn(Long clientId, Collection<Provider> providers);

    /**
     * Find a ClientProvider using its constitent parts
     *
     * @param clientId   to find
     * @param providerId to find
     * @return the ClientProvider or null
     */
    ClientProvider findByClientIdAndProviderId(Long clientId, Long providerId);
}
