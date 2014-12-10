package com.emmisolutions.emmimanager.web.rest.model.provider;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A teamProviderResource
 */
@XmlRootElement(name = "team-provider")
public class TeamProviderResource extends ResourceSupport {

    private ProviderResource provider;

    private TeamProvider entity;

	private Long id;

    private Integer version;

    public TeamProvider getEntity() {
		return entity;
	}

	public void setEntity(TeamProvider entity) {
		this.entity = entity;
	}
	
    public ProviderResource getProvider() {
		return provider;
	}

	public void setProvider(ProviderResource provider) {
		this.provider = provider;
	}

	/**
	 * TeamProviderResource constructor
	 */
	public TeamProviderResource() {
    }

	/**
	 * TeamProviderResource constructor
	 * @param entity
	 * @param providerResource
	 */
    public TeamProviderResource(TeamProvider entity, ProviderResource providerResource) {
        this.id = entity.getId();
        this.version = entity.getVersion();
        this.provider = providerResource;
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
