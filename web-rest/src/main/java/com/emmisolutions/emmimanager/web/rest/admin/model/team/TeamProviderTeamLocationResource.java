package com.emmisolutions.emmimanager.web.rest.admin.model.team;

import com.emmisolutions.emmimanager.web.rest.admin.model.provider.TeamProviderResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a Tag entity.
 */

@XmlRootElement(name = "teamProviderTeamLocation")
public class TeamProviderTeamLocationResource extends ResourceSupport {

	private Long teamProviderTeamLocationId;

	private TeamLocationResource teamLocation;

	private TeamProviderResource teamProvider;

	public Long getTeamProviderTeamLocationId() {
		return teamProviderTeamLocationId;
	}

	public void setTeamProviderTeamLocationId(Long teamProviderTeamLocationId) {
		this.teamProviderTeamLocationId = teamProviderTeamLocationId;
	}

	public TeamLocationResource getTeamLocation() {
		return teamLocation;
	}

	public void setTeamLocation(TeamLocationResource teamLocation) {
		this.teamLocation = teamLocation;
	}

	public TeamProviderResource getTeamProvider() {
		return teamProvider;
	}

	public void setTeamProvider(TeamProviderResource teamProvider) {
		this.teamProvider = teamProvider;
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
