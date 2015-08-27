package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * The email configuration for the client's team. 
  */
@Audited
@Entity
@Table(name = "client_team_email_configuration")
public class ClientTeamEmailConfiguration extends AbstractAuditingEntity {
	/**
     * Default constructor
     */
    public ClientTeamEmailConfiguration() {

    }
    
	/**
     * Constructor with id
     *
     * @param id the id
     */
    public ClientTeamEmailConfiguration(Long id) {
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
    private EmailReminderType type;
	
	@NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_team_id", referencedColumnName="id")
    private Team team;
    
    @Column(name ="rank", columnDefinition = "integer")
   	private Integer rank;
    
    @Column(name = "email_config", columnDefinition = "boolean", nullable = false)
    private boolean emailConfig;

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
	
	public EmailReminderType getType() {
		return type;
	}

	public void setType(EmailReminderType type) {
		this.type = type;
	}

	public boolean isEmailConfig() {
		return emailConfig;
	}

	public void setEmailConfig(boolean emailConfig) {
		this.emailConfig = emailConfig;
	}

	public Integer getRank() {
		return rank;
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
                + emailConfig + " ,team="
                + team + ", type=" + type + '}';
    }
	
	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClientTeamEmailConfiguration that = (ClientTeamEmailConfiguration) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }
	
}