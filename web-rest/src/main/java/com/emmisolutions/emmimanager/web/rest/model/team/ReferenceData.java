package com.emmisolutions.emmimanager.web.rest.model.team;

import com.emmisolutions.emmimanager.web.rest.model.salesforce.SalesForceSearchResponseResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Common reference data for Team operations.
 */
@XmlRootElement(name = "reference-data")
public class ReferenceData extends ResourceSupport {

    public ReferenceData(){
       add(SalesForceSearchResponseResource.createFindTeamLink());
    }

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
