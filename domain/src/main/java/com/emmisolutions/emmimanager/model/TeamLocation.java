package com.emmisolutions.emmimanager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

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

    public TeamLocation(Location location, Team team) {
		super();
		this.location = location;
		this.team = team;
	}

	public TeamLocation() {
		// TODO Auto-generated constructor stub
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
        return "Location{" +
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
    
}
