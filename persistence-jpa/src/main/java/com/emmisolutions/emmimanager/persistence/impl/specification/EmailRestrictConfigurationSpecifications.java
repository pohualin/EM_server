package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration_;

/**
 * This is the specification class that allows for filtering of
 * EmailRestrictConfiguration objects.
 */
@Component
public class EmailRestrictConfigurationSpecifications {

    /**
     * Method to generate Specification with clientRestrictConfigurationId
     * 
     * @param clientRestrictConfigurationId
     *            to use
     * @return Specification with clientRestrictConfigurationId
     */
    public Specification<EmailRestrictConfiguration> isClientRestrictConfiguration(
            final Long clientRestrictConfigurationId) {
        return new Specification<EmailRestrictConfiguration>() {
            @Override
            public Predicate toPredicate(Root<EmailRestrictConfiguration> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (clientRestrictConfigurationId != null) {
                    return cb
                            .equal(root
                                    .get(EmailRestrictConfiguration_.clientRestrictConfiguration),
                                    clientRestrictConfigurationId);
                }
                return null;
            }
        };
    }

}
