package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRestrictConfiguration;

/**
 * Persistence API for ClientRestrictConfiguration
 */
public interface ClientRestrictConfigurationPersistence {

    /**
     * Delete an existing ClientRestrictConfiguration
     * 
     * @param id
     *            to delete
     */
    public void delete(Long id);

    /**
     * Find ClientRestrictConfiguration by passed in Client
     * 
     * @param client
     *            to lookup
     * @return an existing ClientRestrictConfiguration
     */
    public ClientRestrictConfiguration findByClient(Client client);

    /**
     * Reload an existing ClientRestrictConfiguration by id
     * 
     * @param id
     *            to reload
     * @return an existing ClientRestrictConfiguration
     */
    public ClientRestrictConfiguration reload(Long id);

    /**
     * Save or update a ClientRestrictConfiguration
     * 
     * @param clientRestrictConfiguration
     *            to save or update
     * @return saved or updated clientRestrictConfiguration
     */
    public ClientRestrictConfiguration saveOrUpdate(
            ClientRestrictConfiguration clientRestrictConfiguration);
}
