package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for PatientSelfRegConfig Entities
 */
public interface PatientSelfRegConfigurationRepository extends
        JpaRepository<PatientSelfRegConfig, Long>,
        JpaSpecificationExecutor<PatientSelfRegConfig> {

    PatientSelfRegConfig findByTeamId(Long teamId);
}

