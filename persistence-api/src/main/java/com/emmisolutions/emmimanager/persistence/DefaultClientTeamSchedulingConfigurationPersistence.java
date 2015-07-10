package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamSchedulingConfiguration;

/**
 * Default Client Team Scheduling Configuration Persistence
 */
public interface DefaultClientTeamSchedulingConfigurationPersistence {

    /**
     * Find active DefaultClientTeamSchedulingConfiguration
     * 
     * @return active DefaultClientTeamSchedulingConfiguration
     */
    public DefaultClientTeamSchedulingConfiguration findActive();

    /**
     * Reload DefaultClientTeamSchedulingConfiguration by id
     * 
     * @param id
     *            to reload
     * @return an existing DefaultClientTeamSchedulingConfiguration
     */
    public DefaultClientTeamSchedulingConfiguration reload(Long id);

    /**
     * Save or update DefaultClientTeamSchedulingConfiguration
     * 
     * @param defaultPasswordConfiguration
     *            to save or update
     * @return the saved or updated DefaultClientTeamSchedulingConfiguration
     */
    public DefaultClientTeamSchedulingConfiguration saveOrUpdate(
            DefaultClientTeamSchedulingConfiguration defaultPasswordConfiguration);

}
