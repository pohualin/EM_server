package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfig_;
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
public class PatientIdLabelConfigSpecifications {

    /**
     * match on clientId within the provided GroupSearchFilter
     *
     * @param searchFilter GroupSearchFilter
     * @return the specification as a filter predicate
     */
    public Specification<PatientIdLabelConfig> byPatientSelfRegConfig(final PatientIdLabelConfigSearchFilter searchFilter) {
        return new Specification<PatientIdLabelConfig>() {
            @Override
            public Predicate toPredicate(Root<PatientIdLabelConfig> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getPatientSelfRegConfig() != null) {
                    return cb.equal(root.get(PatientIdLabelConfig_.patientSelfRegConfig), searchFilter.getPatientSelfRegConfig());
                }
                return null;
            }
        };
    }

}
