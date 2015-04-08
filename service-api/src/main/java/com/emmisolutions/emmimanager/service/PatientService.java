package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    /**
     * searches for patients with given search query
     *
     * @param page
     * @param filter
     * @return
     */
    Page<Patient> list(Pageable page, PatientSearchFilter filter);

}
