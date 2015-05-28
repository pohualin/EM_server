package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration_;

/**
 * This is the specification class that allows for filtering of ReferenceGroup
 * objects.
 * @param <DefaultClientTeamEmailConfiguration>
 */
@Component
public class DefaultClientTeamEmailConfigurationSpecifications{

	/**
     * adding "active  = true" to where clause
     */
    public Specification<DefaultClientTeamEmailConfiguration> isActive() {
        return new Specification<DefaultClientTeamEmailConfiguration>() {
            @Override
            public Predicate toPredicate(Root<DefaultClientTeamEmailConfiguration> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
            	return cb.isTrue(root.get(DefaultClientTeamEmailConfiguration_.active));
            }
        };
    }

}
