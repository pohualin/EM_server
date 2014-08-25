package com.emmisolutions.emmimanager.web.rest.model.location;

import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.model.State;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Reference data for location editing
 */
@XmlRootElement(name = "reference-data")
public class LocationReferenceData extends ResourceSupport {

    @XmlElement(name = "state")
    @XmlElementWrapper(name = "states")
    private State[] states = State.values();

    @XmlElement(name = "statusFilter")
    @XmlElementWrapper(name = "statusFilters")
    private LocationSearchFilter.StatusFilter[] statusFilters = LocationSearchFilter.StatusFilter.values();


    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        if (super.getLinks().isEmpty()){
            return null;
        }
        return super.getLinks();
    }

}
