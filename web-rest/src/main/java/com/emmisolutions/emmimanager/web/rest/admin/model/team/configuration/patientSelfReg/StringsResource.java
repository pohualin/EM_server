package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg;

import com.emmisolutions.emmimanager.model.Strings;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS Language resource wrapper
 */
@XmlRootElement(name = "strings")
@XmlAccessorType(XmlAccessType.FIELD)
public class StringsResource extends BaseResource<Strings> {

}
