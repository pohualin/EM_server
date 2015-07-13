package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.model.Team;

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

}
