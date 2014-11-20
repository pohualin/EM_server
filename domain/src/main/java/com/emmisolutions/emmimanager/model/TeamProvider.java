package com.emmisolutions.emmimanager.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A TeamProvider.
 */
@Audited
@Table(name = "team_provider")
@Entity
@XmlRootElement(name = "team_provider")
public class TeamProvider extends AbstractAuditingEntity implements
		Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "team_id")
	@XmlElement(name = "team")
	@XmlElementWrapper(name = "team")
	@JsonProperty("team")
	private Team team;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "provider_id")
	@XmlElement(name = "provider")
	@XmlElementWrapper(name = "provider")
	@JsonProperty("provider")
	private Provider provider;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy="teamProvider")
    @JsonManagedReference
    @XmlElement(name = "teamProviderTeamLocations")
	@XmlElementWrapper(name = "teamProviderTeamLocations")
	@JsonProperty("teamProviderTeamLocations")
    private Set<TeamProviderTeamLocation> teamProviderTeamLocations;

    @Length(max = 255)
    @Column(name = "external_id", length = 255, columnDefinition = "nvarchar(255)")
    private String externalId;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

    public Set<TeamProviderTeamLocation> getTeamProviderTeamLocations() {
        return teamProviderTeamLocations;
    }

    public void setTeamProviderTeamLocations(Set<TeamProviderTeamLocation> teamProviderTeamLocations) {
        this.teamProviderTeamLocations = teamProviderTeamLocations;
    }

    public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamProvider that = (TeamProvider) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TeamProvider{" +
            "id=" + id +
            ", version=" + version +
            ", team=" + team +
            ", provider=" + provider +
            '}';
    }
    
    /**
     * Create a TeamProvider from its required parts
     *
     * @param team   the team
     * @param provider the provider
     */
    public TeamProvider(Team team, Provider provider) {
        this.team = team;
        this.provider = provider;
    }
    
    public TeamProvider() {
    }
}
