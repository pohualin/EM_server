package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration_;
import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;

/**
 * This is the specification class that allows for filtering of
 * DefaultPasswordConfiguration objects.
 */
@Component
public class DefaultPasswordConfigurationSpecifications {

    /**
     * adding "active  = true" to where clause
     */
    public Specification<DefaultPasswordConfiguration> isActive() {
        return new Specification<DefaultPasswordConfiguration>() {
            @Override
            public Predicate toPredicate(
                    Root<DefaultPasswordConfiguration> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(DefaultPasswordConfiguration_.active),
                        true);
            }
        };
    }
}