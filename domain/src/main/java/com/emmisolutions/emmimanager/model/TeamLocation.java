package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Set;

/**
 * A Team location.
 * This is the Many to Many relation between Location and Team
 */
@Audited
@Entity
@Table(name = "client_team_location")
@XmlRootElement(name = "client_team_location")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamLocation extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy="teamProvider")
    @XmlTransient
    private Set<TeamProviderTeamLocation> teamProviderTeamLocations;

    /**
     * create a TeamLocation based on a Location and a Team
     * @param location The Location associated to the TeamLocation
     * @param team The Team associated to the TeamLocation
     */
    public TeamLocation(Location location, Team team) {
		super();
		this.location = location;
		this.team = team;
	}

	public TeamLocation() {
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamLocation location = (TeamLocation) o;
        return !(id != null ? !id.equals(location.id) : location.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TeamLocation{" +
                "id=" + id +
                '}';
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

    public Set<TeamProviderTeamLocation> getTeamProviderTeamLocations() {
        return teamProviderTeamLocations;
    }

    public void setTeamProviderTeamLocations(Set<TeamProviderTeamLocation> teamProviderTeamLocations) {
        this.teamProviderTeamLocations = teamProviderTeamLocations;
    }
}
