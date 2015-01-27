package com.emmisolutions.emmimanager.persistence.impl.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration_;

/**
 * This is the specification class that allows for filtering of
 * IpRestrictConfiguration objects.
 */
@Component
public class IpRestrictConfigurationSpecifications {

    /**
     * Method to generate Specification with clientId
     * 
     * @param clientId
     *            to use
     * @return Specification with clientId
     */
    public Specification<IpRestrictConfiguration> isClient(final Long clientId) {
        return new Specification<IpRestrictConfiguration>() {
            @Override
            public Predicate toPredicate(Root<IpRestrictConfiguration> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (clientId != null) {
                    return cb.equal(root.get(IpRestrictConfiguration_.client),
                            clientId);
                }
                return null;
            }
        };
    }

}
