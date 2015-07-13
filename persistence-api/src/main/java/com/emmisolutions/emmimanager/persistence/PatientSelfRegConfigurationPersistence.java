package com.emmisolutions.emmimanager.persistence;


import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;

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

}
