package com.emmisolutions.emmimanager.web.rest.model.team;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.model.BaseResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamsResource;
import org.springframework.hateoas.Link;
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

    public static Link createTeamByTeamIdLink(Long clientId) {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(TeamsResource.class).getTeam(clientId, 1L);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("teamByTeamId");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(TeamsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(TeamsResource.class, method).replace("{clientId}", "" + clientId),
                    link.getRel());
        }
        return null;
    }

}