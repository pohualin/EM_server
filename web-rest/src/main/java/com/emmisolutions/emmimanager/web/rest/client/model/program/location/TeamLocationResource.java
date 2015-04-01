package com.emmisolutions.emmimanager.web.rest.client.model.program.location;

import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * A team location.
 */
@XmlRootElement(name = "team-location")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamLocationResource extends BaseResource<TeamLocation> {

    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        return super.getLinks();
    }

}
