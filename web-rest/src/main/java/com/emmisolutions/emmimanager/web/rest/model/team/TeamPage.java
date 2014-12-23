package com.emmisolutions.emmimanager.web.rest.model.team;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of TeamResource objects.
 */
@XmlRootElement(name = "team-page")
public class TeamPage extends PagedResource<TeamResource> {

    @XmlElement(name = "filter")
    private TeamSearchFilter searchFilter;

    public TeamPage() {
    }

    /**
     * Creates a page for a team search result
     *
     * @param teamResourceSupports to be wrapped
     * @param teamPage             true page
     * @param filter               which caused the response
     */
    public TeamPage(PagedResources<TeamResource> teamResourceSupports, Page<Team> teamPage, TeamSearchFilter filter) {
        pageDefaults(teamResourceSupports, teamPage);
        if (filter != null) {
            addFilterToLinks(filter);
        }
    }

    /**
     * Create the search link
     *
     * @return Link for team searches
     * @see com.emmisolutions.emmimanager.web.rest.resource.TeamsResource#list(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, String, org.springframework.data.web.PagedResourcesAssembler, String...)
     */
    public static Link createFullSearchLink() {
        Link link = linkTo(methodOn(TeamsResource.class).list(null, null, null, null, (String[]) null)).withRel("teams");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    /**
     * Find team by normalized name
     *
     * @param client for a client
     * @return link
     */
    public static Link createFindTeamByNormalizedNameLink(Client client) {
        Link link = linkTo(methodOn(TeamsResource.class).findByNormalizedNameForClient(null, null, client.getId())).withRel("findByNormalizedName");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("normalizedName", TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(TeamSearchFilter filter) {
        this.searchFilter = filter;
        if (CollectionUtils.isEmpty(links)) {
            return;
        }
        // re-write the links to include the filters
        List<Link> existingLinks = links;
        this.links = new ArrayList<>();
        for (Link link : existingLinks) {
            String rel = link.getRel();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(link.getHref());
            if (link.isTemplated()) {
                // add args to template
                UriTemplate uriTemplate = new UriTemplate(link.getHref())
                    .with(new TemplateVariables(
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values
                if (filter.getStatus() != null) {
                    builder.queryParam("status", filter.getStatus());
                }
                if (!CollectionUtils.isEmpty(filter.getNames())) {
                    for (String s : filter.getNames()) {
                        builder.queryParam("name", s);
                    }
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }

    public static Link createFullSearchLinkTeamsWithNoTeamTags(Client entity) {
        Link link = linkTo(methodOn(TeamsResource.class).teamsWithNoTeamTags(entity.getId(), null, null, null)).withRel("teamsWithNoTeamTags");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }
}
