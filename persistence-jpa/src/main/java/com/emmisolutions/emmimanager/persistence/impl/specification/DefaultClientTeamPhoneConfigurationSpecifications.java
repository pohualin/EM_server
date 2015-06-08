package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration_;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration_;

/**
 * This is the specification class that allows for filtering of ReferenceGroup
 * objects.
 * @param <DefaultClientTeamPhoneConfiguration>
 */
@Component
public class DefaultClientTeamPhoneConfigurationSpecifications{

	/**
     * adding "active  = true" to where clause
     */
    public Specification<DefaultClientTeamPhoneConfiguration> isActive() {
        return new Specification<DefaultClientTeamPhoneConfiguration>() {
            @Override
            public Predicate toPredicate(Root<DefaultClientTeamPhoneConfiguration> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
            	return cb.isTrue(root.get(DefaultClientTeamPhoneConfiguration_.active));
            }
        };
    }

}
