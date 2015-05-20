package com.emmisolutions.emmimanager.web.rest.client.model.team;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.client.resource.PatientsResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.ProgramsResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.SchedulesResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static com.emmisolutions.emmimanager.web.rest.client.resource.ProgramsResource.*;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED;
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

        ret.add(new Link(addPaginationTemplate(linkTo(methodOn(ProgramsResource.class)
                .possiblePrograms(entity.getClient().getId(), entity.getId(), null, null, null))
                .withRel("programs").getHref()).with(
                new TemplateVariables(new TemplateVariable(SPECIALTY_ID_REQUEST_PARAM,
                        REQUEST_PARAM_CONTINUED))), "programs"));

        ret.add(new Link(addPaginationTemplate(linkTo(methodOn(ProgramsResource.class)
                .specialties(entity.getClient().getId(), entity.getId(), null, null))
                .withRel("specialties").getHref()), "specialties"));

        ret.add(new Link(addPaginationTemplate(linkTo(methodOn(ProgramsResource.class)
                .locations(entity.getClient().getId(), entity.getId(), null, null, null))
                .withRel("locations").getHref()).with(
                new TemplateVariables(new TemplateVariable(TEAM_PROVIDER_ID_REQUEST_PARAM,
                        REQUEST_PARAM_CONTINUED))), "locations"));

        ret.add(new Link(addPaginationTemplate(linkTo(methodOn(ProgramsResource.class)
                .providers(entity.getClient().getId(), entity.getId(), null, null, null))
                .withRel("providers").getHref()).with(
                new TemplateVariables(new TemplateVariable(TEAM_LOCATION_ID_REQUEST_PARAM,
                        REQUEST_PARAM_CONTINUED))), "providers"));

        ret.add(createPatientFullSearchLink(entity));
        ret.add(linkTo(methodOn(PatientsResource.class).create(entity.getClient().getId(), entity.getId(), null)).withRel("patient"));

        ret.add(new Link(
                new UriTemplate(
                        linkTo(methodOn(PatientsResource.class).get(entity.getClient().getId(), entity.getId(), null)).withSelfRel().getHref())
                        .with(new TemplateVariables(
                                new TemplateVariable("patientId",
                                        TemplateVariable.VariableType.REQUEST_PARAM))), "patientById"));


        return ret;
    }


    private UriTemplate addPaginationTemplate(String baseUri) {
        return new UriTemplate(baseUri)
                .with(new TemplateVariables(
                        new TemplateVariable("page",
                                TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("sort",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
    }


    /**
     * Link for patient search
     *
     * @return Link for patient search
     */
    public static Link createPatientFullSearchLink(Team team) {
        Link link = linkTo(methodOn(PatientsResource.class).list(team.getClient().getId(), null, null, null, team.getId())).withRel("patients");
        UriTemplate uriTemplate = new UriTemplate(link.getHref()).with(
                new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

}
