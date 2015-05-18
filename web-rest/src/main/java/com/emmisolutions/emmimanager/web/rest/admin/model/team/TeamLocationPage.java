package com.emmisolutions.emmimanager.web.rest.admin.model.team;

import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamLocationSearchFilter;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of TeamLocationsResource objects.
 */
@XmlRootElement(name = "team-location-page")
public class TeamLocationPage extends PagedResource<TeamLocationResource> {

    @XmlElement(name = "filter")
    private TeamLocationSearchFilter searchFilter;

    public TeamLocationPage() {
    }

    /**
     * Wrapper for teamTag resource objects
     *
     * @param teamLocationResourceSupports to be wrapped
     * @param teamLocationPage             true page
     * @param filter                       the filter
     */

    public TeamLocationPage(PagedResources<TeamLocationResource> teamLocationResourceSupports, Page<TeamLocation> teamLocationPage, TeamLocationSearchFilter filter) {
        pageDefaults(teamLocationResourceSupports, teamLocationPage);
        addFilterToLinks(filter);
    }

    private void addFilterToLinks(TeamLocationSearchFilter filter) {
        this.searchFilter = filter;
    }
}
