package com.emmisolutions.emmimanager.web.rest.client.model.schedule;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * A scheduled program resource assembler
 */
@Component
public class ScheduledProgramResourceAssembler implements ResourceAssembler<ScheduledProgram, ScheduledProgramResource> {

    @Override
    public ScheduledProgramResource toResource(ScheduledProgram entity) {
        ScheduledProgramResource ret = new ScheduledProgramResource();
        ret.setEntity(entity);
        return ret;
    }
}
