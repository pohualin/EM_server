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
	
	@NotNull
    @OneToOne
    @JoinColumn(name = "client_team_id", nullable = false)
    private Team team;
	
	@Column(name = "collect_phone", columnDefinition = "boolean", nullable = false)
    private boolean collectPhone;
    
    @Column(name = "require_phone", columnDefinition = "boolean", nullable = false)
    private boolean requirePhone;

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
	
	
	public boolean isCollectPhone() {
		return collectPhone;
	}

	public void setCollectPhone(boolean collectPhone) {
		this.collectPhone = collectPhone;
	}

	public boolean isRequirePhone() {
		return requirePhone;
	}

	public void setRequirePhone(boolean requirePhone) {
		this.requirePhone = requirePhone;
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
                + " ,collect_phone=" + collectPhone 
                + " ,require_phone=" + requirePhone
                + " ,team="
                + team +'}';
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