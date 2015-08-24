package com.emmisolutions.emmimanager.persistence.repo;

import java.util.List;

import com.emmisolutions.emmimanager.model.program.ContentSubscription;

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
public interface ContentSubscriptionRepository extends 
				 JpaRepository<ContentSubscription, Long>,
				 JpaSpecificationExecutor<ContentSubscription> {
	
	/**
     * Find all active DefaultClientTeamEmailConfiguration
     *
     * @param specification should be active
     * @param pageable page specification
     * @return an EmailTemplate object or null
     */
	@Override
	Page<ContentSubscription> findAll(Specification<ContentSubscription> specification, Pageable pageable);
}
