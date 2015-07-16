package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * The PatientIdLabelConfig Service
 */
public interface PatientIdLabelConfigService {

    PatientIdLabelConfig create(
            PatientIdLabelConfig patientIdLabelConfig);

    PatientIdLabelConfig update(
            PatientIdLabelConfig patientIdLabelConfig);

    Page<PatientIdLabelConfig> list(Pageable page, PatientIdLabelConfigSearchFilter patientIdLabelConfigSearchFilter);

    PatientIdLabelConfig reload(PatientIdLabelConfig patientIdLabelConfig);

}
