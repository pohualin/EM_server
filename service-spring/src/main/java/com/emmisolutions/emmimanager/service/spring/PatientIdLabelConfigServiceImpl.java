package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import com.emmisolutions.emmimanager.persistence.PatientIdLabelConfigPersistence;
import com.emmisolutions.emmimanager.persistence.PatientSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.PatientIdLabelConfigService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Service layer for PatientIdLabelConfig
 */
@Service
public class PatientIdLabelConfigServiceImpl implements
        PatientIdLabelConfigService {

    @Resource
    PatientSelfRegConfigurationPersistence patientSelfRegConfigurationPersistence;

    @Resource
    PatientIdLabelConfigPersistence patientIdLabelConfigPersistence;

    @Override
    @Transactional
    public PatientIdLabelConfig create(
            PatientIdLabelConfig patientIdLabelConfig) {
        if (patientIdLabelConfig == null) {
            throw new InvalidDataAccessApiUsageException(
                    "patientIdLabelConfig can not be null.");
        }

        patientIdLabelConfig.setId(null);
        patientIdLabelConfig.setVersion(null);
        patientIdLabelConfig.setPatientSelfRegConfig(patientSelfRegConfigurationPersistence.reload(patientIdLabelConfig.getPatientSelfRegConfig().getId()));
        return patientIdLabelConfigPersistence.save(patientIdLabelConfig);
    }

    @Override
    @Transactional
    public PatientIdLabelConfig update(
            PatientIdLabelConfig patientIdLabelConfig) {
        if (patientIdLabelConfig == null || patientIdLabelConfig.getId() == null || patientIdLabelConfig.getVersion() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "PatientSelfRegConfig can not be null.");
        }
        patientIdLabelConfig.setPatientSelfRegConfig(patientSelfRegConfigurationPersistence.reload(patientIdLabelConfig.getPatientSelfRegConfig().getId()));
        return patientIdLabelConfigPersistence.save(patientIdLabelConfig);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<PatientIdLabelConfig> list(Pageable pageable, PatientIdLabelConfigSearchFilter searchFilter) {
        return patientIdLabelConfigPersistence.list(pageable, searchFilter);
    }

    @Override
    public PatientIdLabelConfig reload(PatientIdLabelConfig patientIdLabelConfig) {
        return patientIdLabelConfigPersistence.reload(patientIdLabelConfig);
    }
}
