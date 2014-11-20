package com.emmisolutions.emmimanager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A TeamProvider TeamLocation This is the Many to Many relation between
 * TeamProvider and TeamLocation
 */
@Audited
@Entity
@Table(name = "team_provider_team_location")
@XmlRootElement(name = "team_provider_team_location")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamProviderTeamLocation extends AbstractAuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;
	
	@NotNull
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "team_provider_id", nullable = false)
	private TeamProvider teamProvider;

	@NotNull
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "team_location_id", nullable = false)
	@XmlElement(name = "teamLocation")
	@XmlElementWrapper(name = "teamLocation")
	@JsonProperty("teamLocation")
	private TeamLocation teamLocation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TeamProvider getTeamProvider() {
		return teamProvider;
	}

	public void setTeamProvider(TeamProvider teamProvider) {
		this.teamProvider = teamProvider;
	}

	public TeamLocation getTeamLocation() {
		return teamLocation;
	}

	public void setTeamLocation(TeamLocation teamLocation) {
		this.teamLocation = teamLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TeamProviderTeamLocation other = (TeamProviderTeamLocation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TeamProviderTeamLocation [id=" + id + ", teamProvider="
				+ teamProvider + ", teamLocation=" + teamLocation + "]";
	}
}
