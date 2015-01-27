package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;

/**
 * Service to deal with ClientRestrictConfiguration
 */
public interface ClientRestrictConfigurationService {

    /**
     * Create a new ClientRestrictConfiguration
     * 
     * @param clientRestrictConfiguration
     *            to create
     * @return created ClientRestrictConfiguration
     */
    public ClientRestrictConfiguration create(
            ClientRestrictConfiguration clientRestrictConfiguration);

    /**
     * Delete an existing ClientRestrictConfiguration
     * 
     * @param clientRestrictConfiguration
     *            to delete
     */
    public void delete(ClientRestrictConfiguration clientRestrictConfiguration);

    /**
     * Get ClientRestrictConfiguration by passed in Client
     * 
     * @param client
     *            to lookup
     * @return an existing ClientRestrictConfiguration
     */
    public ClientRestrictConfiguration getByClient(Client client);

    /**
     * Reload an existing ClientRestrictConfiguration
     * 
     * @param clientRestrictConfiguration
     *            to reload
     * @return an existing ClientRestrictConfiguration
     */
    public ClientRestrictConfiguration reload(
            ClientRestrictConfiguration clientRestrictConfiguration);

    /**
     * Update an existing ClientRestrictConfiguration
     * 
     * @param clientRestrictConfiguration
     *            to update
     * @return an updated ClientRestrictConfiguration
     */
    public ClientRestrictConfiguration update(
            ClientRestrictConfiguration clientRestrictConfiguration);

}
