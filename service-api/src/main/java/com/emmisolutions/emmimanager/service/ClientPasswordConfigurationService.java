package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;

/**
 * The ClientPasswordConfiguration Service
 */
public interface ClientPasswordConfigurationService {

    /**
     * Delete the existing ClientPasswordConfiguration
     */
    public ClientPasswordConfiguration delete(
            ClientPasswordConfiguration clientPasswordConfiguration);

    /**
     * Get the ClientPasswordConfiguration for a Client
     * 
     * @param client
     *            to lookup
     * @return an existing ClientPasswordConfiguration or compose one from
     *         DefaultPasswordConfiguration
     */
    public ClientPasswordConfiguration get(Client client);

    /**
     * Reload ClientPasswordConfiguration by id
     * 
     * @param clientPasswordConfiguration
     *            to reload
     * @return an existing ClientPasswordConfiguration
     */
    public ClientPasswordConfiguration reload(
            ClientPasswordConfiguration clientPasswordConfiguration);

    /**
     * Save a ClientPasswordConfiguration
     * 
     * @param clientPasswordConfiguration
     *            to save
     * @return saved ClientPasswordConfiguration
     */
    public ClientPasswordConfiguration save(
            ClientPasswordConfiguration clientPasswordConfiguration);
}
