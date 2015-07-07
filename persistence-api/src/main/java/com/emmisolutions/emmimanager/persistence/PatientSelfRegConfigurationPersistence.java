package com.emmisolutions.emmimanager.persistence;


import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;

/**
 * Persistence API for Client Team Self Registration Configuration.
 */
public interface PatientSelfRegConfigurationPersistence {

    PatientSelfRegConfig save(PatientSelfRegConfig patientSelfRegConfig);

    PatientSelfRegConfig reload(Long id);

}
