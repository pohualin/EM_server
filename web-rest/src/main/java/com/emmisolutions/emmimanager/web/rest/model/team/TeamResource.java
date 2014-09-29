package com.emmisolutions.emmimanager.web.rest.model.team;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.model.BaseResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamsResource;

/**
 * A HATEOAS wrapper for a Team entity.
 */
@XmlRootElement(name = "team")
public class TeamResource extends BaseResource<Team> {
	
	 public static Link createFindTeamByNormalizedNameLink() {
	        Link link = linkTo(methodOn(TeamsResource.class).findByNormalizedNameForClient(null,null,null)).withRel("findTeamByNormalizedName");
	        UriTemplate uriTemplate = new UriTemplate(link.getHref())
	                .with(new TemplateVariables(
	                        new TemplateVariable("normalizedName", TemplateVariable.VariableType.REQUEST_PARAM),
	                        new TemplateVariable("clientId", TemplateVariable.VariableType.REQUEST_PARAM)));
	        return new Link(uriTemplate, link.getRel());
	    }
}
