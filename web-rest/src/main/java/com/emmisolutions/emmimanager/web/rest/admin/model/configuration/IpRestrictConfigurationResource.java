package com.emmisolutions.emmimanager.web.rest.admin.model.configuration;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HATEOAS wrapper for IpRestrictConfiguration, essentially a DTO instead of
 * a wrapper.
 */
@XmlRootElement(name = "ip-restrict-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class IpRestrictConfigurationResource extends ResourceSupport {

    private IpRestrictConfiguration entity;

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

    public IpRestrictConfiguration getEntity() {
        return entity;
    }

    public void setEntity(IpRestrictConfiguration entity) {
        this.entity = entity;
    }

}
