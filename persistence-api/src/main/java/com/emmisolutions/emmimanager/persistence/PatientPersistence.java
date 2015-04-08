package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    /**
     * Fetches a page of Patient objects
     *
     * @param page         defines which page or null for the first page
     * @param searchFilter to filter
     * @return a page of patient objects
     */
    Page<Patient> list(Pageable page, PatientSearchFilter searchFilter);

}
