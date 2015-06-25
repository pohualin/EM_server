package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * The scheduling configuration for the client's team. 
  */
@Audited
@Entity
@Table(name = "client_team_scheduling_configuration")
public class ClientTeamSchedulingConfiguration extends AbstractAuditingEntity {
	/**
     * Default constructor
     */
    public ClientTeamSchedulingConfiguration() {

    }
    
	/**
     * Constructor with id
     *
     * @param id the id
     */
    public ClientTeamSchedulingConfiguration(Long id) {
        this.id = id;
    }
    
    @Version
    private Integer version;
  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;
	
	@NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_team_id", referencedColumnName="id")
    private Team team;
    
    @Column(name = "use_provider", columnDefinition = "boolean", nullable = false)
    private boolean useProvider;
    
    @Column(name = "use_location", columnDefinition = "boolean", nullable = false)
    private boolean useLocation;
    
    public boolean isUseProvider() {
		return useProvider;
	}

	public boolean isUseLocation() {
		return useLocation;
	}

	public void setUseLocation(boolean useLocation) {
		this.useLocation = useLocation;
	}

	public void setUseProvider(boolean useProvider) {
		this.useProvider = useProvider;
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
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

  	@Override
    public String toString() {
        return "ClientTeamSchedulingConfiguration{" + "id=" + id
                + "useProvider=" + useProvider + "useLocation=" + useLocation
                + " ,team=" + team + '}';
    }
	
	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClientTeamSchedulingConfiguration that = (ClientTeamSchedulingConfiguration) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }
	
}