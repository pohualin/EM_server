package com.emmisolutions.emmimanager.persistence;


import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Persistence API for Client Team Self Registration Configuration.
 */
public interface PatientIdLabelConfigPersistence {

    PatientIdLabelConfig save(PatientIdLabelConfig patientIdLabelConfig);

    Page<PatientIdLabelConfig> list(Pageable pageable, PatientIdLabelConfigSearchFilter searchFilter);

    PatientIdLabelConfig reload(PatientIdLabelConfig patientIdLabelConfig);

}
