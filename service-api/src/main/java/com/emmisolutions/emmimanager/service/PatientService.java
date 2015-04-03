package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Patient;

/**
 * Interface for Patient Service
 */
public interface PatientService {

    /**
     * Creates a new patient
     *
     * @param patient
     * @return
     */
    Patient create(Patient patient);

    /**
     * Updates a given patient
     *
     * @param patient
     * @return
     */
    Patient update(Patient patient);

    /**
     * Reloads a given patient
     *
     * @param patient
     * @return
     */
    Patient reload(Patient patient);


}
