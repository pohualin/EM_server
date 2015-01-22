package com.emmisolutions.emmimanager.web.rest.admin.model.team;


import com.emmisolutions.emmimanager.model.TeamLocation;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a Tag entity.
 */
@XmlRootElement(name = "teamLocation")
public class TeamLocationResource extends ResourceSupport {

    private TeamLocation entity;

    public TeamLocation getEntity() {
        return entity;
    }

    public void setEntity(TeamLocation entity) {
        this.entity = entity;
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
