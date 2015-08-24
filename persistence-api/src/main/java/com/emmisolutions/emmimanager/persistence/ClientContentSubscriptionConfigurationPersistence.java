package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;

/**
 * Persistence API for Client Content Subscription Configuration.
 */
public interface ClientContentSubscriptionConfigurationPersistence {

	/**
     * Delete an ClientContentSubscriptionConfiguration
     * 
     * @param id
     *            to delete
     *
     */
    void delete(Long id);
    
	/**
     * Find a page of client team configuration
     *
     * @param clientId for which to find the content subscription configuration
     * @return a ClientContentSubscriptionConfiguration objects
     */
    Page<ClientContentSubscriptionConfiguration> findByClient(Long clientId, Pageable pageable);

    /**
     * Create/Update a client team phone configuration
     *
     * @param clientContentSubscriptionConfiguration to be saved
     * @return the saved ClientContentSubscriptionConfiguration object
     */
    ClientContentSubscriptionConfiguration saveOrUpdate(ClientContentSubscriptionConfiguration clientContentSubscritpionConfiguration);

    /**
     * Reloads a ClientContentSubscriptionConfiguration from persistence
     *
     * @param id to reload
     * @return the persistent ClientContentSubscriptionConfiguration object or null if the ClientContentSubscriptionConfiguration is null or not found
     */
    ClientContentSubscriptionConfiguration reload(Long id);

}
