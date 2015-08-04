package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The search filter for InfoHeaderConfig entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class InfoHeaderConfigSearchFilter {

    private PatientSelfRegConfig patientSelfRegConfig;

    public InfoHeaderConfigSearchFilter(PatientSelfRegConfig patientSelfRegConfig) {
        this.patientSelfRegConfig = patientSelfRegConfig;
    }

    public PatientSelfRegConfig getPatientSelfRegConfig() {
        return patientSelfRegConfig;
    }

    public void setPatientSelfRegConfig(PatientSelfRegConfig patientSelfRegConfig) {
        this.patientSelfRegConfig = patientSelfRegConfig;
    }

    @Override
    public String toString() {
        return "InfoConfigHeaderSearchFilter{" +
                "patientSelfRegConfig=" + patientSelfRegConfig +
                '}';
    }
}
