package com.emmisolutions.emmimanager.web.rest.model.clientlocation;

import com.emmisolutions.emmimanager.web.rest.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a ClientLocation entity.
 */
@XmlRootElement(name = "clientLocation")
public class ClientLocationResource extends ResourceSupport{

    private LocationResource location;

    private ClientResource client;

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


    public LocationResource getLocation() {
        return location;
    }

    public void setLocation(LocationResource location) {
        this.location = location;
    }

    public ClientResource getClient() {
        return client;
    }

    public void setClient(ClientResource client) {
        this.client = client;
    }
}
