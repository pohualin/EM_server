package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Link between a client users and team roles for a specific team. Think of this
 * as a ternary object joining user, role and team.
 */
@Audited
@Entity
@Table(name = "client_team_email_configuration", uniqueConstraints = @UniqueConstraint(columnNames = {
        "client_id", "client_team_id", "team_email_configuration_id"}, name = "uk_client_team_email_configuration"))
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
  
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;
    
    @Column(name = "team_email_configuration_id", columnDefinition = "int", nullable = false)
    private Long teamEmailConfigurationId;
    
    @NotNull
    @Column(columnDefinition = "nvarchar(255)", nullable = false)
    private String description;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_team_id", nullable = false, columnDefinition = "bigint", foreignKey = @ForeignKey(name = "fk_client_team"))
    private Team team;
    
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false, columnDefinition = "bigint", foreignKey = @ForeignKey(name = "fk_client"))
    private Client client;
    
    public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	@Column(name ="rank", columnDefinition = "integer")
   	private Integer rank;
    
    @Column(name = "value", columnDefinition = "boolean", nullable = false)
    private boolean value;

   

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

  	public Long getTeamEmailConfigurationId() {
		return teamEmailConfigurationId;
	}

	public void setTeamEmailConfigurationId(Long teamEmailConfigurationId) {
		this.teamEmailConfigurationId = teamEmailConfigurationId;
	}

	@Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
    public String toString() {
        return "ClientTeamEmailConfiguration{" + "id=" + id
                + ", description=" + description + " ,value=" +
        		value + " ,team="
                + team + ", client=" + client + '}';
    }
}