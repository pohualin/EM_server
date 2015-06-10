package com.emmisolutions.emmimanager.web.rest.client.model.team;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.client.resource.PatientsResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.ProgramsResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.SchedulesResource;

import org.springframework.hateoas.*;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

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
        ret.add(new Link(addPaginationTemplate(
                linkTo(methodOn(SchedulesResource.class)
                        .scheduled(entity.getClient().getId(), entity.getId(), null, null))
                        .withSelfRel().getHref()), "schedulePrograms"));

        // create special template link to allow to find a schedule by id for a team
        ret.add(createScheduleByIdLink(entity));
        
        ret.add(linkTo(methodOn(PatientsResource.class).findTeamEmailConfigForPatient(entity.getClient().getId(), entity.getId(), null, null)).withRel("patientTeamEmailConfig"));

        ret.add(new Link(addPaginationTemplate(linkTo(methodOn(ProgramsResource.class)
                .possiblePrograms(entity.getClient().getId(), entity.getId(), null, null, null))
                .withSelfRel().getHref()).with(
                new TemplateVariables(new TemplateVariable(SPECIALTY_ID_REQUEST_PARAM,
                        REQUEST_PARAM_CONTINUED))), "programs"));

        ret.add(new Link(
                addPaginationTemplate(
                        linkTo(methodOn(ProgramsResource.class)
                                .specialties(entity.getClient().getId(), entity.getId(), null, null))
                                .withSelfRel().getHref()), "specialties"));

        ret.add(new Link(addPaginationTemplate(linkTo(methodOn(ProgramsResource.class)
                .locations(entity.getClient().getId(), entity.getId(), null, null, null))
                .withSelfRel().getHref()).with(
                new TemplateVariables(new TemplateVariable(TEAM_PROVIDER_ID_REQUEST_PARAM,
                        REQUEST_PARAM_CONTINUED))), "locations"));

        ret.add(new Link(addPaginationTemplate(linkTo(methodOn(ProgramsResource.class)
                .providers(entity.getClient().getId(), entity.getId(), null, null, null))
                .withSelfRel().getHref()).with(
                new TemplateVariables(new TemplateVariable(TEAM_LOCATION_ID_REQUEST_PARAM,
                        REQUEST_PARAM_CONTINUED))), "providers"));

        ret.add(createPatientFullSearchLink(entity));
        ret.add(createAllPatientsScheduledLink(entity));
        ret.add(linkTo(methodOn(PatientsResource.class)
                .create(entity.getClient().getId(), entity.getId(), null)).withRel("patient"));

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
                        new TemplateVariable("size",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
    }


    /**
     * Link for patient search
     *
     * @return Link for patient search
     */
    private Link createPatientFullSearchLink(Team team) {
        Link link = linkTo(methodOn(PatientsResource.class).list(team.getClient().getId(), null, null, null, team.getId())).withRel("patients");
        UriTemplate uriTemplate = new UriTemplate(link.getHref()).with(
                new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }


    /**
     * Link for patient search
     *
     * @return Link for patient search
     */
    private Link createAllPatientsScheduledLink(Team team) {
        Link link = linkTo(methodOn(PatientsResource.class).listAllPatientsScheduledForTeam(team.getClient().getId(), null, null, team.getId())).withRel("patientsScheduled");
        UriTemplate uriTemplate = new UriTemplate(link.getHref()).with(
                new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    /**
     * Creates a template link that points to the 'load a scheduled program by id' method
     *
     * @return the link
     * @see SchedulesResource#aScheduled(Long, Long, Long)
     */
    private Link createScheduleByIdLink(Team team) {
        DummyInvocationUtils.LastInvocationAware invocations =
                (DummyInvocationUtils.LastInvocationAware) methodOn(SchedulesResource.class)
                        .aScheduled(team.getClient().getId(), team.getId(), 1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("scheduleById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(SchedulesResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(SchedulesResource.class, method),
                    link.getRel());
        }
        return null;
    }

    private static final MappingDiscoverer discoverer = new AnnotationMappingDiscoverer(RequestMapping.class);
}
