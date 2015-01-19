package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.configuration.PasswordConfiguration;

/**
 * Password Configuration Persistence
 */
public interface PasswordConfigurationPersistence {

    /**
     * Reload PasswordConfiguration by passed in id
     * 
     * @param id
     *            to reload
     * @return an existing PasswordConfiguration
     */
    public PasswordConfiguration reload(Long id);

    /**
     * Save or update the passed in PasswordConfiguration
     * 
     * @param passwordConfiguration
     *            to save or update
     * @return the saved or updated PasswordConfiguration
     */
    public PasswordConfiguration saveOrUpdate(
            PasswordConfiguration passwordConfiguration);

}
