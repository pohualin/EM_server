package com.emmisolutions.emmimanager.web.rest.client.model.program.location;

import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

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
                                    Page<TeamLocation> teamLocations) {
        pageDefaults(teamLocationResources, teamLocations);
    }
}
