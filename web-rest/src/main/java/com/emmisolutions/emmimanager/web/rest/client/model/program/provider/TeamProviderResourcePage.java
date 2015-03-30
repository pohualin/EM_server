package com.emmisolutions.emmimanager.web.rest.client.model.program.provider;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

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
                                    Page<TeamProvider> teamProviders) {
        pageDefaults(teamProviderResources, teamProviders);
    }
}
