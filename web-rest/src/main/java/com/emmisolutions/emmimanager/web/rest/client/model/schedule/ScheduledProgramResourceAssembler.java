package com.emmisolutions.emmimanager.web.rest.client.model.schedule;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.web.rest.client.resource.SchedulesResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A scheduled program resource assembler
 */
@Component
public class ScheduledProgramResourceAssembler implements ResourceAssembler<ScheduledProgram, ScheduledProgramResource> {

    @Override
    public ScheduledProgramResource toResource(ScheduledProgram entity) {
        ScheduledProgramResource ret = new ScheduledProgramResource();
        ret.add(linkTo(methodOn(SchedulesResource.class).aScheduled(entity.getTeam().getClient().getId(),
                entity.getTeam().getId(), entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}
