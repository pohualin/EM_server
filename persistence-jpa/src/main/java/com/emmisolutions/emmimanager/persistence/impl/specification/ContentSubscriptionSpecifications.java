package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.model.program.ContentSubscription_;

/**
 * This is the specification class that allows for filtering of
 * ContentSubscription objects.
 */
@Component
public class ContentSubscriptionSpecifications {

    /**
     * adding "active  = true" to where clause
     */
    public Specification<ContentSubscription> isActive() {
        return new Specification<ContentSubscription>() {
            @Override
            public Predicate toPredicate(
                    Root<ContentSubscription> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root
                        .get(ContentSubscription_.active),
                        true);
            }
        };
    }
}