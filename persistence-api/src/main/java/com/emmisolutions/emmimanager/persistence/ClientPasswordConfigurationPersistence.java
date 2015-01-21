package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;

/**
 * Client Password Configuration Persistence
 */
public interface ClientPasswordConfigurationPersistence {

    /**
     * Delete existing ClientPasswordConfiguration by id
     * 
     * @param id
     *            to delete
     */
    public void delete(Long id);

    /**
     * Find ClientPasswordConfiguration by Client
     * 
     * @param client
     *            to use
     * @return null or an existing ClientPasswordConfiguration
     */
    public ClientPasswordConfiguration findByClient(Client client);

    /**
     * Reload ClientPasswordConfiguration by passed in id
     * 
     * @param id
     *            to reload
     * @return an existing ClientPasswordConfiguration
     */
    public ClientPasswordConfiguration reload(Long id);

    /**
     * Save or update the passed in ClientPasswordConfiguration
     * 
     * @param clientPasswordConfiguration
     *            to save or update
     * @return the saved or updated ClientPasswordConfiguration
     */
    public ClientPasswordConfiguration saveOrUpdate(
            ClientPasswordConfiguration clientPasswordConfiguration);

}
