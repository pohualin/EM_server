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
     * Find all the active DefaultTeamPhoneConfiguration
     * 
     * @return page of Default Team Phone Configuration
     */
    Page<DefaultClientTeamPhoneConfiguration> findActive(Pageable page);
      
}
