package com.emmisolutions.emmimanager.persistence;


import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Persistence API for PatientIdLabelConfig
 */
public interface PatientIdLabelConfigPersistence {

    /**
     * saved a given patientIdLabelConfig
     *
     * @param patientIdLabelConfig to save
     * @return patientIdLabelConfig
     */
    PatientIdLabelConfig save(PatientIdLabelConfig patientIdLabelConfig);

    /**
     * returns a page of patientIdLabelConfigs based on page size and search filter
     *
     * @param pageable     page size
     * @param searchFilter search criteria
     * @return page of patientIdLabelConfigs
     */
    Page<PatientIdLabelConfig> list(Pageable pageable, PatientIdLabelConfigSearchFilter searchFilter);

    /**
     * reloads a given patientIdLabelConfig
     *
     * @param patientIdLabelConfig to reload
     * @return patientIdLabelConfig
     */
    PatientIdLabelConfig reload(PatientIdLabelConfig patientIdLabelConfig);

}
