package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * The PatientIdLabelConfig Service
 */
public interface PatientIdLabelConfigService {
    /**
     * Creates a new patient id label config
     *
     * @param patientIdLabelConfig to create
     * @return saved patientIdLabelConfig
     */
    PatientIdLabelConfig create(
            PatientIdLabelConfig patientIdLabelConfig);

    /**
     * Updates a given patient id label config
     *
     * @param patientIdLabelConfig to update
     * @return updated patientIdLabelConfig
     */
    PatientIdLabelConfig update(
            PatientIdLabelConfig patientIdLabelConfig);

    /**
     * Gets a list of all patient id label configs based on search filter
     *
     * @param page
     * @param patientIdLabelConfigSearchFilter
     * @return
     */
    Page<PatientIdLabelConfig> list(Pageable page, PatientIdLabelConfigSearchFilter patientIdLabelConfigSearchFilter);

    /**
     * Reloads a given patient id label config
     *
     * @param patientIdLabelConfig to reload
     * @return reloaded patientIdLabelConfig
     */
    PatientIdLabelConfig reload(PatientIdLabelConfig patientIdLabelConfig);

}
