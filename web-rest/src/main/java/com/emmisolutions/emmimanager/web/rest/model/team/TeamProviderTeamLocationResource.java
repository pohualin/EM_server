package com.emmisolutions.emmimanager.web.rest.model.team;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.web.rest.model.provider.TeamProviderResource;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A HATEOAS wrapper for a Tag entity.
 */
@XmlRootElement(name = "teamProviderTeamLocation")
public class TeamProviderTeamLocationResource extends ResourceSupport {

	private TeamLocationResource location;

	private TeamProviderResource provider;

	public TeamLocationResource getLocation() {
		return location;
	}

	public void setLocation(TeamLocationResource location) {
		this.location = location;
	}

	public TeamProviderResource getProvider() {
		return provider;
	}

	public void setProvider(TeamProviderResource provider) {
		this.provider = provider;
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
