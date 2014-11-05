package com.emmisolutions.emmimanager.web.rest.model.team;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamLocationSearchFilter;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamLocationsResource;

/**
 * A HATEOAS wrapper for a page of TeamLocationsResource objects.
 */
@XmlRootElement(name = "team-location-page")
public class TeamLocationPage extends PagedResource<TeamLocationResource> {

    @XmlElement(name = "filter")
    private TeamLocationSearchFilter searchFilter;

    public TeamLocationPage() {
    }

    /**
     * Wrapper for teamTag resource objects
     *
     * @param teamLocationResourceSupports to be wrapped
     * @param teamLocationPage             true page
     */

    public TeamLocationPage(PagedResources<TeamLocationResource> teamLocationResourceSupports, Page<TeamLocation> teamLocationPage, TeamLocationSearchFilter filter) {
        pageDefaults(teamLocationResourceSupports, teamLocationPage);
        addFilterToLinks(filter);
    }

    /**
     * Create the search link
     *
     * @return Link for team locations searches
     */
    public static Link createFullSearchLink(Team team) {
        Link link = linkTo(methodOn(TeamLocationsResource.class).list(team.getId(), null, null, null, null, null)).withRel("teamLocations");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(TeamLocationSearchFilter filter) {
        this.searchFilter = filter;
    }
}
