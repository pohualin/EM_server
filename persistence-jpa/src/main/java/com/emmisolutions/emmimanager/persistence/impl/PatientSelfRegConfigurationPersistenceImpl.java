package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.PatientSelfRegConfigurationRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Persistence implementation for ClientTeamSelfRegConfiguration entity.
 */
@Repository
public class PatientSelfRegConfigurationPersistenceImpl implements
        PatientSelfRegConfigurationPersistence {

    @Resource
    PatientSelfRegConfigurationRepository patientSelfRegConfigurationRepository;

    @Override
    public PatientSelfRegConfig save(
            PatientSelfRegConfig patientSelfRegConfig) {
        return patientSelfRegConfigurationRepository.save(patientSelfRegConfig);
    }

    @Override
    public PatientSelfRegConfig reload(Long id) {
        if (id == null) {
            return null;
        }
        return patientSelfRegConfigurationRepository.findOne(id);
    }

    @Override
    public PatientSelfRegConfig findByTeamId(Long teamId) {
        if (teamId == null) {
            return null;
        }
        return patientSelfRegConfigurationRepository.findByTeamId(teamId);
    }

}
