package com.emmisolutions.emmimanager.web.rest.client.model.team;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.client.resource.ProgramsResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.SchedulesResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Converts UserClientUserClientTeamRole objects to TeamResource objects with the proper links governed by
 * the Role.
 */
@Component("clientTeamResourceAssembler")
public class TeamResourceAssembler
        implements ResourceAssembler<Team, TeamResource> {

    @Override
    public TeamResource toResource(Team entity) {
        TeamResource ret = new TeamResource();
        ret.setEntity(entity);
        ret.add(linkTo(methodOn(SchedulesResource.class)
                .schedule(entity.getClient().getId(), entity.getId(), null, null, null))
                .withRel("schedulePrograms"));

        ret.add(new Link(new UriTemplate(linkTo(methodOn(ProgramsResource.class)
                .possiblePrograms(entity.getClient().getId(), entity.getId(), null, null))
                .withRel("programs").getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page",
                                TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable(
                                "sort",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED))), "programs"));

        return ret;
    }


}
