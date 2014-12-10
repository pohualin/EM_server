package com.emmisolutions.emmimanager.web.rest.model.provider;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.Provider;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HATEOAS wrapper for Provider entities
 */
@XmlRootElement(name = "provider")
public class ProviderResource extends ResourceSupport {

    private Provider entity;

    public Provider getEntity() {
        return entity;
    }

    public void setEntity(Provider entity) {
        this.entity = entity;
    }

	public ProviderResource(Provider entity) {
		super();
		this.entity = entity;
	}
	
	public ProviderResource() {
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
