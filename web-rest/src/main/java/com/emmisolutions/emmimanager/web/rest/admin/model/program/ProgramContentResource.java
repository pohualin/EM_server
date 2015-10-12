package com.emmisolutions.emmimanager.web.rest.admin.model.program;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A program.
 */
@XmlRootElement(name = "program")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProgramContentResource extends BaseResource<Program> {


}
