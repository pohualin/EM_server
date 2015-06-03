package com.emmisolutions.emmimanager.persistence.repo;

import java.util.List;

import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * Default Client Team EmailConfiguration repo
 *
 */
public interface DefaultClientTeamEmailConfigurationRepository extends 
				 JpaRepository<DefaultClientTeamEmailConfiguration, Long>,
				 JpaSpecificationExecutor<DefaultClientTeamEmailConfiguration> {
	
	/**
     * Find all active DefaultClientTeamEmailConfiguration
     *
     * @param specification should be active
     * @param pageable page specification
     * @return an EmailTemplate object or null
     */
	@Override
	Page<DefaultClientTeamEmailConfiguration> findAll(Specification<DefaultClientTeamEmailConfiguration> specification, Pageable pageable);
  
}
