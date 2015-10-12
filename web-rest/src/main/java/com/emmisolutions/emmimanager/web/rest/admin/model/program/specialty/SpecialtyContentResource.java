package com.emmisolutions.emmimanager.web.rest.admin.model.program.specialty;

import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS specialty resource wrapper
 */
@XmlRootElement(name = "specialty")
@XmlAccessorType(XmlAccessType.FIELD)
public class SpecialtyContentResource extends BaseResource<Specialty> {

}
