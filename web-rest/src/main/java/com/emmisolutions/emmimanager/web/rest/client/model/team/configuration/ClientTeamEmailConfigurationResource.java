package com.emmisolutions.emmimanager.web.rest.client.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;

import java.util.List;

/**
 * HATEOAS wrapper for ClientTeamEmailConfiguration, essentially a DTO instead of
 * a wrapper.
 */
@XmlRootElement(name = "client-team-email-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientTeamEmailConfigurationResource extends ResourceSupport {

    private ClientTeamEmailConfiguration entity;

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

    public ClientTeamEmailConfiguration getEntity() {
        return entity;
    }

    public void setEntity(ClientTeamEmailConfiguration entity) {
        this.entity = entity;
    }

}
