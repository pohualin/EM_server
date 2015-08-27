package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
