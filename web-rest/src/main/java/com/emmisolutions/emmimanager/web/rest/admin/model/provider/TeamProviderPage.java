package com.emmisolutions.emmimanager.web.rest.admin.model.provider;

import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamProvidersResource;
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
 * A HATEOAS wrapper for a page of TeamProviderResource objects.
 */
@XmlRootElement(name = "team-provider-page")
public class TeamProviderPage extends PagedResource<TeamProviderResource> {

    @XmlElement(name = "filter")
    private ProviderSearchFilter searchFilter;

    public TeamProviderPage() {
    }

    /**
     * Wrapped constructor
     *
     * @param providerResources to be wrapped
     * @param providerPage      the raw response
     */
    public TeamProviderPage(PagedResources<TeamProviderResource> providerResources, Page<TeamProvider> providerPage) {
        pageDefaults(providerResources, providerPage);
    }

    /**
     * TeamProviderPage constructor
     *
     * @param providerResources to be wrapped
     * @param teamProviderPage  loaded from the back-end
     * @param filter            the filter
     */
    public TeamProviderPage(PagedResources<TeamProviderResource> providerResources, Page<TeamProvider> teamProviderPage, ProviderSearchFilter filter) {
        pageDefaults(providerResources, teamProviderPage);
        addFilterToLinks(filter);

    }

    /**
     * Link to get TeamProvider by id
     *
     * @param teamProviderId the team provider id
     * @return Link for get teamProvider by id
     */

    public static Link createProviderByIdLink(Long teamProviderId) {
        Link link = linkTo(methodOn(TeamProvidersResource.class).getById(teamProviderId)).withRel("findProviderById");
        UriTemplate uriTemplate = new UriTemplate(link.getHref());
        return new Link(uriTemplate, link.getRel());
    }

    public static Link createTeamProviderTeamLocationLink(TeamProvider teamProvider) {
        Link link = linkTo(methodOn(TeamProvidersResource.class).createTeamProviderTeamLocation(null, teamProvider.getId())).withRel("teamProviderTeamLocation");
        UriTemplate uriTemplate = new UriTemplate(link.getHref());
        return new Link(uriTemplate, link.getRel());
    }

    /**
     * Create the search link
     *
     * @param team the given team
     * @return Link for provider searches
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.TeamProvidersResource#possible(Long, org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler, String, String)
     */
    public static Link createAssociationLink(Team team) {
        Link link = linkTo(methodOn(TeamProvidersResource.class).possible(team.getId(), null, null, null, null, null)).withRel("possibleProviders");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("name", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(ProviderSearchFilter filter) {
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
}
