package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;

/**
 * Default Password Configuration Persistence
 */
public interface DefaultPasswordConfigurationPersistence {

    /**
     * Find active DefaultPasswordConfiguration
     * 
     * @return active DefaultPasswordConfiguration
     */
    public DefaultPasswordConfiguration findActive();

    /**
     * Reload DefaultPasswordConfiguration by passed in id
     * 
     * @param id
     *            to reload
     * @return an existing DefaultPasswordConfiguration
     */
    public DefaultPasswordConfiguration reload(Long id);

    /**
     * Save or update the passed in DefaultPasswordConfiguration
     * 
     * @param defaultPasswordConfiguration
     *            to save or update
     * @return the saved or updated DefaultPasswordConfiguration
     */
    public DefaultPasswordConfiguration saveOrUpdate(
            DefaultPasswordConfiguration defaultPasswordConfiguration);

}
