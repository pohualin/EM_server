package com.emmisolutions.emmimanager.web.rest.admin.model.team;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamProvidersResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamsResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Method;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a Team entity.
 */
@XmlRootElement(name = "team")
public class TeamResource extends BaseResource<Team> {


    private static final MappingDiscoverer discoverer = new AnnotationMappingDiscoverer(RequestMapping.class);

    /**
     * Load a team by it's id
     *
     * @param client to use
     * @return the team load link
     */
    public static Link createTeamByTeamIdLink(Client client) {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(TeamsResource.class).getTeam(client.getId(), 1L);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("teamByTeamId");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(TeamsResource.class));
        if (idx != -1) {
            return new Link(
                href.substring(0, idx) + discoverer.getMapping(TeamsResource.class, method).replace("{clientId}", "" + client.getId()),
                link.getRel());
        }
        return null;
    }
    
	 public static Link getByProviderAndTeam(Team team){
		 Link link = linkTo(methodOn(TeamProvidersResource.class).getByProviderAndTeam(team.getId(), null)).withRel("findTeamProviderByProviderAndTeam");
		 UriTemplate uriTemplate = new UriTemplate(link.getHref()).with(new TemplateVariables(
				 new TemplateVariable("providerId", TemplateVariable.VariableType.REQUEST_PARAM)));
		 return new Link(uriTemplate, link.getRel());
	 }

    public static Link clientTeams(Client client){
        Link link = linkTo(methodOn(TeamsResource.class).clientTeams(client.getId(), null, null, null, null, null, (String[]) null)).withRel("teams");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("teamTagsType", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));

        return new Link(uriTemplate, link.getRel());
	 }

}
