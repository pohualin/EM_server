package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.PatientIdLabelType;
import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.model.Strings;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.PatientIdLabelTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.PatientSelfRegConfigurationRepository;
import com.emmisolutions.emmimanager.persistence.repo.StringsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Persistence implementation for PatientSelfRegConfiguration entity.
 */
@Repository
public class PatientSelfRegConfigurationPersistenceImpl implements
        PatientSelfRegConfigurationPersistence {

    @Resource
    PatientSelfRegConfigurationRepository patientSelfRegConfigurationRepository;

    @Resource
    PatientIdLabelTypeRepository patientIdLabelTypeRepository;

    @Resource
    StringsRepository stringsRepository;

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

    @Override
    public Collection<PatientIdLabelType> getAllPatientIdLabelTypes() {
        return patientIdLabelTypeRepository.findAll();
    }

    @Override
    public Page<Strings> findByString(String key, Pageable page) {
        return stringsRepository.findByKey(key, page);
    }
}
