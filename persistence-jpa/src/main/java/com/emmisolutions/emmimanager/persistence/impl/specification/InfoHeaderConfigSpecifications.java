package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import com.emmisolutions.emmimanager.model.InfoHeaderConfig_;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * This is the specification class that allows for filtering of InfoHeaderConfig objects.
 */
@Component
public class InfoHeaderConfigSpecifications {

    @Resource
    PatientSelfRegConfigurationPersistence patientSelfRegConfigurationPersistence;

    /**
     * match on patientSelfRegConfig within the provided InfoHeaderConfigSearchFilter
     *
     * @param searchFilter InfoHeaderConfigSearchFilter
     * @return the specification as a filter predicate
     */
    public Specification<InfoHeaderConfig> byPatientSelfRegConfig(final InfoHeaderConfigSearchFilter searchFilter) {
        return new Specification<InfoHeaderConfig>() {
            @Override
            public Predicate toPredicate(Root<InfoHeaderConfig> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (searchFilter != null && searchFilter.getPatientSelfRegConfig() != null) {
                    return cb.equal(root.get(InfoHeaderConfig_.patientSelfRegConfig), patientSelfRegConfigurationPersistence.reload(searchFilter.getPatientSelfRegConfig().getId()));
                }
                return null;
            }
        };
    }

}
