package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import com.emmisolutions.emmimanager.persistence.PatientIdLabelConfigPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.PatientIdLabelConfigSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.LanguageRepository;
import com.emmisolutions.emmimanager.persistence.repo.PatientIdLabelConfigRepository;
import com.emmisolutions.emmimanager.persistence.repo.PatientIdLabelTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Persistence implementation for PatientIdLabelConfig entity.
 */
@Repository
public class PatientIdLabelConfigPersistenceImpl implements
        PatientIdLabelConfigPersistence {

    @Resource
    PatientIdLabelConfigRepository patientIdLabelConfigRepository;

    @Resource
    PatientIdLabelConfigSpecifications patientIdLabelConfigSpecifications;

    @Resource
    LanguageRepository languageRepository;

    @Resource
    PatientIdLabelTypeRepository patientIdLabelTypeRepository;

    @Override
    public PatientIdLabelConfig save(
            PatientIdLabelConfig patientIdLabelConfig) {
        patientIdLabelConfig.setLanguage(languageRepository.findByLanguageTag(patientIdLabelConfig.getLanguage().getLanguageTag()));
        patientIdLabelConfig.setIdLabelType(patientIdLabelTypeRepository.findOne(patientIdLabelConfig.getIdLabelType().getId()));
        return patientIdLabelConfigRepository.save(patientIdLabelConfig);
    }

    @Override
    public PatientIdLabelConfig reload(PatientIdLabelConfig patientIdLabelConfig) {
        if (patientIdLabelConfig == null || patientIdLabelConfig.getId() == null) {
            return null;
        }
        return patientIdLabelConfigRepository.findOne(patientIdLabelConfig.getId());
    }

    @Override
    public Page<PatientIdLabelConfig> list(Pageable page, PatientIdLabelConfigSearchFilter searchFilter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return patientIdLabelConfigRepository.findAll(where(patientIdLabelConfigSpecifications.byPatientSelfRegConfig(searchFilter)), page);
    }
}
