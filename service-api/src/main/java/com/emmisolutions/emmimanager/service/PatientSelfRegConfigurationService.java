package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * The PatientSelfRegConfiguration Service
 */
public interface PatientSelfRegConfigurationService {

    /**
     * Save or Update a PatientSelfRegConfigurationService
     *
     * @param patientSelfRegConfig to save
     * @return a associated PatientSelfRegConfig
     */
    PatientSelfRegConfig create(
            PatientSelfRegConfig patientSelfRegConfig);

    /**
     * Save or Update a PatientSelfRegConfigurationService
     *
     * @param patientSelfRegConfig to update
     * @return a associated PatientSelfRegConfig
     */
    PatientSelfRegConfig update(
            PatientSelfRegConfig patientSelfRegConfig);

    /**
     * finds a patient self reg config by given team
     *
     * @param team to find for
     * @return a PatientSelfRegConfig
     */
    PatientSelfRegConfig findByTeam(Team team);

    /**
     * reloads a given patient self reg configuration
     *
     * @param patientSelfRegConfig
     * @return a PatientSelfRegConfig
     */
    PatientSelfRegConfig reload(PatientSelfRegConfig patientSelfRegConfig);

    /**
     * finds all available languages
     *
     * @param page
     * @return page of languages available
     */
    Page<Language> findAllAvailableLanguages(Pageable page);

    Collection<PatientIdLabelType> getAllPatientIdLabelTypes();

    Strings findByLanguageAndString(Language language, PatientIdLabelType type);

    Page<Strings> findByString(String key, Pageable page);

}
