package com.emmisolutions.emmimanager.web.rest.model.team;

import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of TeamTagsResource objects.
 */
@XmlRootElement(name = "team-tag-page")
public class TeamTagPage extends PagedResource<TeamTagResource> {

    public TeamTagPage() {
    }

    /**
     * Wrapper for teamTag resource objects
     *
     * @param teamTagResourceSupports to be wrapped
     * @param teamTagPage             true page
     */

    public TeamTagPage(PagedResources<TeamTagResource> teamTagResourceSupports, Page<TeamTag> teamTagPage) {
        pageDefaults(teamTagResourceSupports, teamTagPage);
    }
}
