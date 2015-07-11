package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamSchedulingConfiguration_;

/**
 * This is the specification class that allows for filtering of
 * DefaultClientTeamSchedulingConfiguration objects.
 */
@Component
public class DefaultClientTeamSchedulingConfigurationSpecifications {

    /**
     * adding "active  = true" to where clause
     */
    public Specification<DefaultClientTeamSchedulingConfiguration> isActive() {
        return new Specification<DefaultClientTeamSchedulingConfiguration>() {
            @Override
            public Predicate toPredicate(
                    Root<DefaultClientTeamSchedulingConfiguration> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root
                        .get(DefaultClientTeamSchedulingConfiguration_.active),
                        true);
            }
        };
    }
}