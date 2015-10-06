package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HATEOAS wrapper for ClientPasswordConfituration, essentially a DTO instead of
 * a wrapper.
 */
@XmlRootElement(name = "client-password-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamPrintInstructionConfigurationResource extends ResourceSupport {

    private TeamPrintInstructionConfiguration entity;

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

    public TeamPrintInstructionConfiguration getEntity() {
        return entity;
    }

    public void setEntity(TeamPrintInstructionConfiguration entity) {
        this.entity = entity;
    }
}
