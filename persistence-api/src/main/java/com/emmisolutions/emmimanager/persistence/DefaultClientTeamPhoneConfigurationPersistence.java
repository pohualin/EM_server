package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;

/**
 * Default Team Phone Configuration persistence
 *
 */
public interface DefaultClientTeamPhoneConfigurationPersistence {

       
    /**
     * Find DefaultTeamPhoneConfiguration
     * 
     * @return Default Team Phone Configuration
     */
    DefaultClientTeamPhoneConfiguration find();
      
}
