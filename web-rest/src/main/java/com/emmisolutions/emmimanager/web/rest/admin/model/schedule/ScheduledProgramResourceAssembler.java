package com.emmisolutions.emmimanager.web.rest.admin.model.schedule;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.resource.AdminSchedulesResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A scheduled program resource assembler
 */
@Component("adminScheduledProgramResourceAssembler")
public class ScheduledProgramResourceAssembler implements ResourceAssembler<ScheduledProgram, ScheduledProgramResource> {

    @Override
    public ScheduledProgramResource toResource(ScheduledProgram entity) {
        ScheduledProgramResource ret = new ScheduledProgramResource();
        ret.add(linkTo(methodOn(AdminSchedulesResource.class).get(entity.getId())).withSelfRel());
        ret.add(linkTo(methodOn(AdminSchedulesResource.class).getNotes(entity.getId())).withRel("programNotes"));
        ret.add(ClientResourceAssembler.createFullUsersSearchLink(entity.getTeam().getClient()).withRel("clientUsers"));
        ret.setEntity(entity);
        return ret;
    }
}
