package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;

/**
 * Default Team Email Configuration persistence
 *
 */
public interface DefaultClientTeamEmailConfigurationPersistence {

    /**
     * Find DefaultClientTeamEmailConfiguration
     *
     * @return a DefaultClientTeamEmailConfiguration
     */
    DefaultClientTeamEmailConfiguration find();

}
