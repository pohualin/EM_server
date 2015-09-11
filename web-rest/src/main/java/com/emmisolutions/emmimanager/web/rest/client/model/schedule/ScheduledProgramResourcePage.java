package com.emmisolutions.emmimanager.web.rest.client.model.schedule;

import static com.emmisolutions.emmimanager.web.rest.client.resource.SchedulesResource.ENCOUNTER_REQUEST_PARAM;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.SchedulesResource;

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
    
    /**
     * Creates link for GET to find SchedulesResource objects
     *
     * @return the link
     */
    public static Link scheduleProgramsSearchLink(Team team) {
        Link link = linkTo(methodOn(SchedulesResource.class)
                .scheduled(team.getClient().getId(), team.getId(), null, null, null)).withRel("schedulePrograms");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", REQUEST_PARAM),
                        new TemplateVariable("size", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort*", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(ENCOUNTER_REQUEST_PARAM, REQUEST_PARAM_CONTINUED)
                ));
        return new Link(uriTemplate, link.getRel());
    }
}
