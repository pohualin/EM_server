package com.emmisolutions.emmimanager.web.rest.client.model.program.provider;

import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.web.rest.client.resource.ProgramsResource.TEAM_LOCATION_ID_REQUEST_PARAM;

/**
 * A page of TeamProviderResource objects
 */
public class TeamProviderResourcePage extends PagedResource<TeamProviderResource> {

    public TeamProviderResourcePage() {
    }

    /**
     * Make a new resource page
     *
     * @param teamProviderResources the paged resources
     * @param teamProviders         the paged entities
     */
    public TeamProviderResourcePage(PagedResources<TeamProviderResource> teamProviderResources,
                                    Page<TeamProvider> teamProviders, TeamLocation teamLocation) {
        pageDefaults(teamProviderResources, teamProviders);
        addRequestParametersToLinks(teamLocation);
    }

    private void addRequestParametersToLinks(TeamLocation teamLocation) {
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
                                new TemplateVariable(TEAM_LOCATION_ID_REQUEST_PARAM,
                                        TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
                        ));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values
                if (teamLocation != null) {
                    builder.queryParam(TEAM_LOCATION_ID_REQUEST_PARAM, teamLocation.getId());
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }
}
