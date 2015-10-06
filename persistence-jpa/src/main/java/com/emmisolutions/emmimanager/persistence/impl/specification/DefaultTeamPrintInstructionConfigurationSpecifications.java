package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultTeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultTeamPrintInstructionConfiguration_;

/**
 * This is the specification class that allows for filtering of
 * DefaultTeamPrintInstructionConfiguration objects.
 */
@Component
public class DefaultTeamPrintInstructionConfigurationSpecifications {

    /**
     * adding "active  = true" to where clause
     */
    public Specification<DefaultTeamPrintInstructionConfiguration> isActive() {
        return new Specification<DefaultTeamPrintInstructionConfiguration>() {
            @Override
            public Predicate toPredicate(
                    Root<DefaultTeamPrintInstructionConfiguration> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root
                        .get(DefaultTeamPrintInstructionConfiguration_.active),
                        true);
            }
        };
    }
}