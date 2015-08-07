package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for PatientSelfRegConfig Entities
 */
public interface PatientSelfRegConfigurationRepository extends
        JpaRepository<PatientSelfRegConfig, Long>,
        JpaSpecificationExecutor<PatientSelfRegConfig> {

    /**
     * finds a patient self reg config by given team
     *
     * @param teamId to find for
     * @return a PatientSelfRegConfig
     */
    PatientSelfRegConfig findByTeamId(Long teamId);
}

