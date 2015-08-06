package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for PatientIdLabelConfig Entities
 */
public interface PatientIdLabelConfigRepository extends
        JpaRepository<PatientIdLabelConfig, Long>,
        JpaSpecificationExecutor<PatientIdLabelConfig> {

}

