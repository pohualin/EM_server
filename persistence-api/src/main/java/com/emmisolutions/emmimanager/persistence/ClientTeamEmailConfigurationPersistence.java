package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;

/**
 * Persistence API for Client Team Email Configuration.
 */
public interface ClientTeamEmailConfigurationPersistence {

    /**
     * Find a page of client team email configuration
     *
     * @param teamId for which to find the email configuration
     * @return a ClientTeamEmailConfiguration object
     */
    ClientTeamEmailConfiguration find(Long teamId);

    /**
     * Create/Update a client team email configuration
     *
     * @param clientTeamEmailConfiguration to be saved
     * @return the saved ClientTeamEmailConfiguration object
     */
    ClientTeamEmailConfiguration save(ClientTeamEmailConfiguration clientTeamEmailConfiguration);

    /**
     * Reloads a ClientTeamEmailConfiguration from persistence
     *
     * @param id to reload
     * @return the persistent ClientTeamEmailConfiguration object or null if the ClientTeamEmailConfiguration is null or not found
     */
    ClientTeamEmailConfiguration reload(Long id);

}
