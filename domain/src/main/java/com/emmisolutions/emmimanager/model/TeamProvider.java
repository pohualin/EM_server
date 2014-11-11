package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

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
	private Team team;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "provider_id")
	private Provider provider;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy="teamProvider")
    @XmlTransient
    private Set<TeamProviderTeamLocation> teamProviderTeamLocations;

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
}
