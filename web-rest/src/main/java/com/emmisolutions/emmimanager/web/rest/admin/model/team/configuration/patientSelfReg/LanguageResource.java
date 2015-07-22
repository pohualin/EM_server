package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg;

import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS Language resource wrapper
 */
@XmlRootElement(name = "specialty")
@XmlAccessorType(XmlAccessType.FIELD)
public class LanguageResource extends BaseResource<Language> {

}
