package com.emmisolutions.emmimanager.web.rest.client.model.program.location;

import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.web.rest.client.resource.ProgramsResource.TEAM_PROVIDER_ID_REQUEST_PARAM;

/**
 * A page of TeamLocationResource objects
 */
public class TeamLocationResourcePage extends PagedResource<TeamLocationResource> {

    public TeamLocationResourcePage() {
    }

    /**
     * Make a new resource page
     *
     * @param teamLocationResources the paged resources
     * @param teamLocations         the paged entities
     */
    public TeamLocationResourcePage(PagedResources<TeamLocationResource> teamLocationResources,
                                    Page<TeamLocation> teamLocations, TeamProvider teamProvider) {
        pageDefaults(teamLocationResources, teamLocations);
        addRequestParametersToLinks(teamProvider);
    }

    private void addRequestParametersToLinks(TeamProvider teamProvider) {
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
                                new TemplateVariable(TEAM_PROVIDER_ID_REQUEST_PARAM,
                                        TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
                        ));
                this.links.add(new Link(uriTemplate.toString(), rel));
            } else {
                // add values
                if (teamProvider != null) {
                    builder.queryParam(TEAM_PROVIDER_ID_REQUEST_PARAM, teamProvider.getId());
                }
                this.links.add(new Link(builder.build().encode().toUriString(), rel));
            }
        }
    }
}
