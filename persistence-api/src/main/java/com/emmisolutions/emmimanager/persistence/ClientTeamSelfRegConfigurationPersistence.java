package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;

/**
 * Persistence API for Client Team Self Registration Configuration.
 */
public interface ClientTeamSelfRegConfigurationPersistence {

    /**
     * Find a page of client team self registration configuration
     *
     * @param teamId for which to find the phone configuration
     * @return a ClientTeamPhoneConfiguration objects
     */
    ClientTeamSelfRegConfiguration find(Long teamId);

    /**
     * Create/Update a client team self registration configuration
     *
     * @param clientTeamSelfRegConfiguration to be saved
     * @return the saved ClientTeamPhoneConfiguration object
     */
    ClientTeamSelfRegConfiguration save(ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration);

    /**
     * Reloads a ClientTeamSelfRegConfiguration from persistence
     *
     * @param id to reload
     * @return the persistent ClientTeamSelfRegConfiguration object or null if the ClientTeamSelfRegConfiguration is null or not found
     */
    ClientTeamSelfRegConfiguration reload(Long id);

    /**
     * Finds a self registration configuration by the code given
     * @param code
     * @return ClientTeamSelfRegConfiguration
     */
    ClientTeamSelfRegConfiguration findByCode(String code);


}
