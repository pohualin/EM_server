package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Default Team Email Configuration persistence
 *
 */
public interface DefaultClientTeamEmailConfigurationPersistence {

       
    /**
     * Find all the active DefaultTeamEmailConfiguration
     * 
     * @return page of Default Team Email Configuration
     */
    Page<DefaultClientTeamEmailConfiguration> findActive(Pageable page);
    
    
    /**
    * reload DefaultTeamEmailConfiguration by passing in an id
    * @param id
    * @return DefaultTeamEmailConfiguration
    */
    DefaultClientTeamEmailConfiguration reload(Long id);
   
}
