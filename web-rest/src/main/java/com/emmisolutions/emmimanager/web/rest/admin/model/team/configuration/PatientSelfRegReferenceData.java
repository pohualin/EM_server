package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.PatientIdLabelType;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Reference data for patient self reg config
 */
@XmlRootElement(name = "patient-self-reg-reference-data")
public class PatientSelfRegReferenceData extends BaseResource<PatientIdLabelType> {

    @XmlElement(name = "idLabelTypes")
    @XmlElementWrapper(name = "idLabelTypes")
    private PatientIdLabelType[] idLabelTypes = PatientIdLabelType.values();
}
