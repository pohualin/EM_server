package com.emmisolutions.emmimanager.web.rest.model.location;

import com.emmisolutions.emmimanager.model.Location;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a Location entity.
 */
@XmlRootElement(name = "location")
public class LocationResource extends ResourceSupport {

    private Location entity;

    public Location getEntity() {
        return entity;
    }

    public void setEntity(Location entity) {
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
