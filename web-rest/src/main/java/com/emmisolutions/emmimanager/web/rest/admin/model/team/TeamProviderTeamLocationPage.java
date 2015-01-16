package com.emmisolutions.emmimanager.web.rest.admin.model.team;

import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of TeamProviderTeamLocationResource objects.
 */
@XmlRootElement(name = "team-provider-team-location-page")

public class TeamProviderTeamLocationPage extends PagedResource<TeamProviderTeamLocationResource> {

    public TeamProviderTeamLocationPage() {
    }

    /**
     * Wrapper for teamTag resource objects
     *
     * @param teamProviderTeamLocationResourceSupports to be wrapped
     * @param teamProviderTeamLocationPage             true page
     */
    public TeamProviderTeamLocationPage(PagedResources<TeamProviderTeamLocationResource> teamProviderTeamLocationResourceSupports, Page<TeamProviderTeamLocation> teamProviderTeamLocationPage) {
        pageDefaults(teamProviderTeamLocationResourceSupports, teamProviderTeamLocationPage);
    }

}
