package com.emmisolutions.emmimanager.web.rest.client.model.schedule;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

/**
 * A page of ScheduledProgramResource objects
 */
public class ScheduledProgramResourcePage extends PagedResource<ScheduledProgramResource> {

    public ScheduledProgramResourcePage() {
    }

    public ScheduledProgramResourcePage(PagedResources<ScheduledProgramResource> scheduledProgramResources,
                                        Page<ScheduledProgram> scheduledPrograms) {
        pageDefaults(scheduledProgramResources, scheduledPrograms);
    }
}
