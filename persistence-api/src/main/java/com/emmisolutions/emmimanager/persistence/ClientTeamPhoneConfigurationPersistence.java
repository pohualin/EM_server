package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;

/**
 * Persistence API for Client Team Phone Configuration.
 */
public interface ClientTeamPhoneConfigurationPersistence {

    /**
     * Find a page of client team phone configuration
     *
     * @param teamId for which to find the phone configuration
     * @return a ClientTeamPhoneConfiguration objects
     */
    ClientTeamPhoneConfiguration find(Long teamId);

    /**
     * Create/Update a client team phone configuration
     *
     * @param clientTeamPhoneConfiguration to be saved
     * @return the saved ClientTeamPhoneConfiguration object
     */
    ClientTeamPhoneConfiguration save(ClientTeamPhoneConfiguration clientTeamPhoneConfiguration);

    /**
     * Reloads a ClientTeamPhoneConfiguration from persistence
     *
     * @param id to reload
     * @return the persistent ClientTeamPhoneConfiguration object or null if the ClientTeamPhoneConfiguration is null or not found
     */
    ClientTeamPhoneConfiguration reload(Long id);

}
