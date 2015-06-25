package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;

/**
 * Persistence API for Client Team Scheduling Configuration.
 */
public interface ClientTeamSchedulingConfigurationPersistence {

    /**
     * Find a page of client team scheduling configuration
     *
     * @param teamId for which to find the scheduling configuration
     * @return a ClientTeamSchedulingConfiguration objects
     */
    ClientTeamSchedulingConfiguration find(Long teamId);

    /**
     * Create/Update a client team scheduling configuration
     *
     * @param clientTeamSchedulingConfiguration to be saved
     * @return the saved ClientTeamSchedulingConfiguration object
     */
    ClientTeamSchedulingConfiguration save(ClientTeamSchedulingConfiguration clientTeamSchedulingConfiguration);

    /**
     * Reloads a ClientTeamSchedulingConfiguration from persistence
     *
     * @param id to reload
     * @return the persistent ClientTeamSchedulingConfiguration object or null if the ClientTeamSchedulingConfiguration is null or not found
     */
    ClientTeamSchedulingConfiguration reload(Long id);

}
