package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.persistence.ContentSubscriptionPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.ContentSubscriptionSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.ContentSubscriptionRepository;

/**
 * Persistence Implementation to handle Content Subscription
 */
@Repository
public class ContentSubscriptionPersistenceImpl implements
				ContentSubscriptionPersistence {

    @Resource
    ContentSubscriptionRepository contentSubscriptionRepository;
    
    @Resource
    ContentSubscriptionSpecifications contentSubscriptionSpecifications;

	@Override
	public Page<ContentSubscription> findActive(Pageable page) {
		return contentSubscriptionRepository
                .findAll(where(contentSubscriptionSpecifications
                        .isActive()),  page != null ? page : new PageRequest(0, 20, Sort.Direction.DESC, "rank"));
	}
  
}
