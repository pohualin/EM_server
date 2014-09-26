package com.emmisolutions.emmimanager.web.rest.model.client;

import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamTagsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
