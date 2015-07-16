package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * This is the specification class that allows for filtering of Group objects.
 */
@Component
public class InfoHeaderConfigSpecifications {

    /**
     * match on clientId within the provided GroupSearchFilter
     *
     * @param searchFilter GroupSearchFilter
     * @return the specification as a filter predicate
     */
    public Specification<InfoHeaderConfig> byPatientSelfRegConfig(final InfoHeaderConfigSearchFilter searchFilter) {
        return new Specification<InfoHeaderConfig>() {
            @Override
            public Predicate toPredicate(Root<InfoHeaderConfig> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getPatientSelfRegConfig() != null) {
                    return cb.equal(root.get(InfoHeaderConfig_.patientSelfRegConfig), searchFilter.getPatientSelfRegConfig());
                }
                return null;
            }
        };
    }

}
