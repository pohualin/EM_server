package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultPasswordConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.DefaultClientTeamEmailConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.impl.specification.DefaultPasswordConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.DefaultPasswordConfigurationRepository;
import com.emmisolutions.emmimanager.persistence.repo.DefaultClientTeamEmailConfigurationRepository;

/**
 * Persistence Implementation to deal with DefaultPasswordConfiguration
 */
@Repository
public class DefaultClientTeamEmailConfigurantionPersistenceImpl implements
				DefaultClientTeamEmailConfigurationPersistence {

    @Resource
    DefaultClientTeamEmailConfigurationRepository defaultClientTeamEmailConfigurationRepository;
    
    @Resource
    DefaultClientTeamEmailConfigurationSpecifications defaultClientTeamEmailConfigurationSpecifications;


    @Override
    public Page<DefaultClientTeamEmailConfiguration> findActive(Pageable page) {
    	if (page == null) {
            page = new PageRequest(0, 10);
        }
        return defaultClientTeamEmailConfigurationRepository
        		.findAll(where(defaultClientTeamEmailConfigurationSpecifications
                        .isActive()), page);
    }

   
}
