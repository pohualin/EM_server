package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientOptOutPreference;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Patient Persistence API
 */
public interface PatientPersistence {

    /**
     * Creates or updates a given patient
     *
     * @param patient to save
     * @return the updated patient
     */
    Patient save(Patient patient);

    /**
     * Reloads a given patient
     *
     * @param patient to reload
     * @return the persistent patient
     */
    Patient reload(Patient patient);

    /**
     * Fetches a page of Patient objects
     *
     * @param page         defines which page or null for the first page
     * @param searchFilter to filter
     * @return a page of patient objects
     */
    Page<Patient> list(Pageable page, PatientSearchFilter searchFilter);

    /**
     * Loads the collection of possible opt out preferences
     *
     * @return collection of preferences
     */
    Collection<PatientOptOutPreference> allPossibleOptOutPreferences();

}
