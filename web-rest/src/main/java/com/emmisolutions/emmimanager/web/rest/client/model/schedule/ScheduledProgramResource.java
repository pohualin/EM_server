package com.emmisolutions.emmimanager.web.rest.client.model.schedule;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A scheduled program.
 */
@XmlRootElement(name = "scheduled-program")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduledProgramResource extends BaseResource<ScheduledProgram> {


}
