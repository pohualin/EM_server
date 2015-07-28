package com.emmisolutions.emmimanager.web.rest.admin.model.team;


import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.web.rest.admin.model.location.LocationResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a TeamLocation entity.
 */
@XmlRootElement(name = "teamLocation")
public class TeamLocationResource extends ResourceSupport {

    private TeamLocation entity;

    private LocationResource location;

    public TeamLocation getEntity() {
        return entity;
    }

    public void setEntity(TeamLocation entity) {
        this.entity = entity;
    }

    public LocationResource getLocation() {
        return location;
    }

    public void setLocation(LocationResource locationResource) {
        this.location = locationResource;
    }

    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks(){
        return super.getLinks();
    }
}
