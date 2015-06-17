package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientOptOutPreference;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Interface for Patient Service
 */
public interface PatientService {

    /**
     * Creates a new patient
     *
     * @param patient to create
     * @return the updated patient
     */
    Patient create(Patient patient);

    /**
     * Updates a given patient
     *
     * @param patient to update
     * @return the updated patient
     */
    Patient update(Patient patient);

    /**
     * Reloads a given patient
     *
     * @param patient to reload
     * @return the persistent patient
     */
    Patient reload(Patient patient);

    /**
     * searches for patients with given search query
     *
     * @param page   specification
     * @param filter for narrowing
     * @return a page of Patient
     */
    Page<Patient> list(Pageable page, PatientSearchFilter filter);

    /**
     * Loads the collection of possible opt out preferences
     *
     * @return collection of preferences
     */
    Collection<PatientOptOutPreference> allPossibleOptOutPreferences();

}
