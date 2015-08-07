package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * HATEOAS wrapper for PatientSelfRegConfig
 */
@XmlRootElement(name = "patient-self-reg-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientSelfRegConfigurationResource extends BaseResource<PatientSelfRegConfig> {
}
