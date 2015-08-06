package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The search filter for PatientIdLabelConfig entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class PatientIdLabelConfigSearchFilter {

    private PatientSelfRegConfig patientSelfRegConfig;

    public PatientIdLabelConfigSearchFilter(PatientSelfRegConfig patientSelfRegConfig) {
        this.patientSelfRegConfig = patientSelfRegConfig;
    }

    public PatientSelfRegConfig getPatientSelfRegConfig() {
        return patientSelfRegConfig;
    }

    public void setPatientSelfRegConfig(PatientSelfRegConfig patientSelfRegConfig) {
        this.patientSelfRegConfig = patientSelfRegConfig;
    }

    public PatientIdLabelConfigSearchFilter() {
    }

    @Override
    public String toString() {
        return "PatientIdLabelConfigSearchFilter{" +
                "patientSelfRegConfig=" + patientSelfRegConfig +
                '}';
    }
}
