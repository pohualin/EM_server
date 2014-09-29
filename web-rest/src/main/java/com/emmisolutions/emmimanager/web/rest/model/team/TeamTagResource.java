package com.emmisolutions.emmimanager.web.rest.model.team;


import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.TeamTag;
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
@XmlRootElement(name = "teamTag")
public class TeamTagResource extends ResourceSupport {

    private TeamTag entity;

    public TeamTag getEntity() {
        return entity;
    }

    public void setEntity(TeamTag entity) {
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
