package com.emmisolutions.emmimanager.web.rest.admin.model.team;

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

    public TeamLocationResourcePage(
            PagedResources<TeamLocationResource> pagedResources,
            Page<TeamLocation> teamLocationPage) {
        pageDefaults(pagedResources, teamLocationPage);
    }

}
