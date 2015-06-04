package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * The phone configuration for the client's team. 
  */
@Audited
@Entity
@Table(name = "client_team_phone_configuration")
public class ClientTeamPhoneConfiguration extends AbstractAuditingEntity {
	/**
     * Default constructor
     */
    public ClientTeamPhoneConfiguration() {

    }
    
	/**
     * Constructor with id
     *
     * @param id the id
     */
    public ClientTeamPhoneConfiguration(Long id) {
        this.id = id;
    }
    
    @Version
    private Integer version;
  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50)
    private PhoneReminderType type;
	
	@NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_team_id", referencedColumnName="id")
    private Team team;
    
    @Column(name ="rank", columnDefinition = "integer")
   	private Integer rank;
    
    @Column(name = "phone_config", columnDefinition = "boolean", nullable = false)
    private boolean phoneConfig;

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
	
	
	public Integer getRank() {
		return rank;
	}

	public boolean isPhoneConfig() {
		return phoneConfig;
	}

	public PhoneReminderType getType() {
		return type;
	}

	public void setType(PhoneReminderType type) {
		this.type = type;
	}

	public void setPhoneConfig(boolean phoneConfig) {
		this.phoneConfig = phoneConfig;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
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
        return "ClientTeamEmailConfiguration{" + "id=" + id
                + " ,phoneConfig=" +phoneConfig + " ,team="
                + team + ", type=" + type + '}';
    }
	
	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClientTeamPhoneConfiguration that = (ClientTeamPhoneConfiguration) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }
	
}