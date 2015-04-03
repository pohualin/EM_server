package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Patient;

/**
 * Patient Persistence API
 */
public interface PatientPersistence {

    /**
     * Creates or updates a given patient
     *
     * @param patient
     * @return
     */
    Patient save(Patient patient);

    /**
     * Reloads a given patient
     *
     * @param patient
     * @return
     */
    Patient reload(Patient patient);

}
