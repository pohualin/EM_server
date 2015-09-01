package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.program.ContentSubscription;

/**
 * Content Subscription Persistence
 */
public interface ContentSubscriptionPersistence {

    /**
     * Find content subscriptions
     *
     * @param pageable the page specification
     * @return a page of content subscriptions
     */
    Page<ContentSubscription> findActive(Pageable pageable);
}
