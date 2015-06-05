package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.DefaultClientTeamPhoneConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.DefaultClientTeamPhoneConfigurationRepository;

/**
 * Persistence Implementation to deal with DefaultClientTeamPhoneConfigurantion
 */
@Repository
public class DefaultClientTeamPhoneConfigurantionPersistenceImpl implements
				DefaultClientTeamPhoneConfigurationPersistence {

    @Resource
    DefaultClientTeamPhoneConfigurationRepository defaultClientTeamPhoneConfigurationRepository;
    
    @Resource
    DefaultClientTeamPhoneConfigurationSpecifications defaultClientTeamPhoneConfigurationSpecifications;

	@Override
	public DefaultClientTeamPhoneConfiguration find() {
		 return defaultClientTeamPhoneConfigurationRepository
	                .findOne(where(defaultClientTeamPhoneConfigurationSpecifications
	                        .isActive()));
	}
  
}
