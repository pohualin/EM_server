package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfig_;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * This is the specification class that allows for filtering of PatientIdLabelConfig objects.
 */
@Component
public class PatientIdLabelConfigSpecifications {

    @Resource
    PatientSelfRegConfigurationPersistence patientSelfRegConfigurationPersistence;

    /**
     * match on patientSelfRegConfig within the provided PatientIdLabelConfigSearchFilter
     *
     * @param searchFilter PatientIdLabelConfigSearchFilter
     * @return the specification as a filter predicate
     */
    public Specification<PatientIdLabelConfig> byPatientSelfRegConfig(final PatientIdLabelConfigSearchFilter searchFilter) {
        return new Specification<PatientIdLabelConfig>() {
            @Override
            public Predicate toPredicate(Root<PatientIdLabelConfig> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getPatientSelfRegConfig() != null) {
                    return cb.equal(root.get(PatientIdLabelConfig_.id), searchFilter.getPatientSelfRegConfig().getId());
                }
                return null;
            }
        };
    }

}
