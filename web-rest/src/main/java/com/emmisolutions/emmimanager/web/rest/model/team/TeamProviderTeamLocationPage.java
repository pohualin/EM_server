package com.emmisolutions.emmimanager.web.rest.model.team;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;

/**
 * A HATEOAS wrapper for a page of TeamLocationsResource objects.
 */
@XmlRootElement(name = "team-provider-team-location-page")
public class TeamProviderTeamLocationPage extends PagedResource<TeamProviderTeamLocationResource> {

    public TeamProviderTeamLocationPage() {
    }

    /**
     * Wrapper for teamTag resource objects
     *
     * @param teamLocationResourceSupports to be wrapped
     * @param teamLocationPage             true page
     */
    public TeamProviderTeamLocationPage(PagedResources<TeamProviderTeamLocationResource> teamProviderTeamLocationResourceSupports, Page<TeamProviderTeamLocation> teamProviderTeamLocationPage) {
        pageDefaults(teamProviderTeamLocationResourceSupports, teamProviderTeamLocationPage);
    }

}
