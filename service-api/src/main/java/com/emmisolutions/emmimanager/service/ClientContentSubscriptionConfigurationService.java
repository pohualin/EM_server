package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.model.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * The ClientContentSubscriptionConfiguration Service
 */
public interface ClientContentSubscriptionConfigurationService {

    /**
     * Update a ClientContentSubscriptionConfiguration
     * 
     * @param clientContentSubscriptionConfigurationn
     *            to update
     * @return a associated ClientContentSubscriptionConfiguration
     */
    public ClientContentSubscriptionConfiguration update(
    		ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration);
    
    /**
     * create ClientContentSubscriptionConfiguration
     * 
     * @param clientContentSubscriptionConfigurationn
     *            to create
     * @return a associated ClientContentSubscriptionConfiguration
     */
    public ClientContentSubscriptionConfiguration create(
    		ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration);

    
    /**
     * Find a Page of existing clientContentSubscriptionConfiguration by Client id 
     * @param clientId
     *            to find
     * @param pageable
     *            to use
     * @return a Page of existing clientContentSubscriptionConfiguration
     */
    public Page<ClientContentSubscriptionConfiguration> findByClient(
            Client client,
            Pageable pageable);
    
    /**
     * Delete an existing ClientContentSubscriptionConfiguration
     * 
     * @param clientContentSubscriptionConfiguration
     *            to delete
     */
    public void delete(Client client);

    /**
     * Reload an existing ClientContentSubscriptionConfiguration
     * 
     * @param clientContentSubscriptionConfiguration
     *            to reload
     * @return an existing ClientContentSubscriptionConfiguration
     */
    public ClientContentSubscriptionConfiguration reload(
    		ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration);

    /**
     * Find a Page of ContentSubscriptions 
     * @param pageable
     *            to use
     * @return a Page of ContentSubscription
     */
	public Page<ContentSubscription> list(Pageable pageable);

     
}
