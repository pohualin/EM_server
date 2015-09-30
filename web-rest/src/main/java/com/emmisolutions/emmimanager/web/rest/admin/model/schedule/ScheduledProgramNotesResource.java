package com.emmisolutions.emmimanager.web.rest.admin.model.schedule;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramNote;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A scheduled program's notes.
 */
@XmlRootElement(name = "scheduled-program")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduledProgramNotesResource extends BaseResource<ScheduledProgramNote> {
}
