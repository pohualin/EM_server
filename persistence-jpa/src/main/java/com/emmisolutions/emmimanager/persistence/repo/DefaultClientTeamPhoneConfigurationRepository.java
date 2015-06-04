package com.emmisolutions.emmimanager.persistence.repo;

import java.util.List;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * Default Client Team PhoneConfiguration repo
 *
 */
public interface DefaultClientTeamPhoneConfigurationRepository extends 
				 JpaRepository<DefaultClientTeamPhoneConfiguration, Long>,
				 JpaSpecificationExecutor<DefaultClientTeamPhoneConfiguration> {
	
	/**
     * Find all active DefaultClientTeamPhoneConfiguration
     *
     * @param specification should be active
     * @param pageable page specification
     * @return an PhoneTemplate object or null
     */
	@Override
	Page<DefaultClientTeamPhoneConfiguration> findAll(Specification<DefaultClientTeamPhoneConfiguration> specification, Pageable pageable);
  
}
