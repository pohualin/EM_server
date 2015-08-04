package com.emmisolutions.emmimanager.persistence;


import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.model.PatientIdLabelType;
import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.model.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Persistence API for Client Team Self Registration Configuration.
 */
public interface PatientSelfRegConfigurationPersistence {

    /**
     * saves a given patient self-reg configuration
     *
     * @param patientSelfRegConfig
     * @return a PatientSelfRegConfig
     */
    PatientSelfRegConfig save(PatientSelfRegConfig patientSelfRegConfig);

    /**
     * Reloads a given patient self-reg configuration
     *
     * @param id
     * @return a PatientSelfRegConfig
     */
    PatientSelfRegConfig reload(Long id);

    /**
     * Finds a patient self-reg configuration by given team id
     *
     * @param teamId
     * @return a PatientSelfRegConfig
     */
    PatientSelfRegConfig findByTeamId(Long teamId);

    /**
     * returns a collection of all patient id label types for reference data
     * @return
     */
    Collection<PatientIdLabelType> getAllPatientIdLabelTypes();

    /**
     * finds strings (translations) by given language and key path
     * @param language
     * @param string
     * @return strings
     */
    Strings findByLanguageAndString(Language language, String string);

    /**
     * reloads patientItLabelType
     * @param patientIdLabelType to reload
     * @return patientIdLabelType
     */
    PatientIdLabelType reloadPatientIdLabelType(PatientIdLabelType patientIdLabelType);

    /**
     * returns a page of strings for a given key
     * @param key
     * @param page
     * @return
     */
    Page<Strings> findByString(String key, Pageable page);


}
