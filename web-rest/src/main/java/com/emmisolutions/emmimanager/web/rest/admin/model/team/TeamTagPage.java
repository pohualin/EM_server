package com.emmisolutions.emmimanager.web.rest.admin.model.team;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.model.TeamTagSearchFilter;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamTagsResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamsResource;
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
 * A HATEOAS wrapper for a page of TeamTagsResource objects.
 */
@XmlRootElement(name = "team-tag-page")
public class TeamTagPage extends PagedResource<TeamTagResource> {

    @XmlElement(name = "filter")
    private TeamTagSearchFilter searchFilter;

    public TeamTagPage() {
    }

    /**
     * Wrapper for teamTag resource objects
     *
     * @param filter                  the filter
     * @param teamTagResourceSupports to be wrapped
     * @param teamTagPage             true page
     */

    public TeamTagPage(PagedResources<TeamTagResource> teamTagResourceSupports, Page<TeamTag> teamTagPage, TeamTagSearchFilter filter) {
        pageDefaults(teamTagResourceSupports, teamTagPage);
        addFilterToLinks(filter);
    }

    /**
     * Create the search link
     *
     * @return Link for team tag searches
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

    public static Link createFullSearchLinkTeamTagsWithTags(Client entity) {
        Link link = linkTo(methodOn(TeamTagsResource.class).teamTagsWithTags(entity.getId(), null, null, null,null,null)).withRel("teamTagsWithTags");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                    new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                    new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                    new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                    new TemplateVariable("tagIds", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                    new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(TeamTagSearchFilter filter) {
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
                                new TemplateVariable("tagIds", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                if (!CollectionUtils.isEmpty(filter.getTagSet())) {
                    for (Tag tag : filter.getTagSet()) {
                        builder.queryParam("tagIds", tag.getId());
                    }
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }
}
