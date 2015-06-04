package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.DefaultClientTeamPhoneConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.DefaultClientTeamPhoneConfigurationRepository;

/**
 * Persistence Implementation to deal with DDefaultClientTeamPhoneConfigurantion
 */
@Repository
public class DefaultClientTeamPhoneConfigurantionPersistenceImpl implements
				DefaultClientTeamPhoneConfigurationPersistence {

    @Resource
    DefaultClientTeamPhoneConfigurationRepository defaultClientTeamPhoneConfigurationRepository;
    
    @Resource
    DefaultClientTeamPhoneConfigurationSpecifications defaultClientTeamPhoneConfigurationSpecifications;


    @Override
    public Page<DefaultClientTeamPhoneConfiguration> findActive(Pageable page) {
    	if (page == null) {
            page = new PageRequest(0, 10);
        }
        return defaultClientTeamPhoneConfigurationRepository
        		.findAll(where(defaultClientTeamPhoneConfigurationSpecifications
                        .isActive()), page);
    }

   
}
