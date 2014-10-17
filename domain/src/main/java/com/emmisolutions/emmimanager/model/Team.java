package com.emmisolutions.emmimanager.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * A Team that corresponds to a Client
 */
@Audited
@Entity
@Table(name = "client_team")
@XmlRootElement(name = "client_team")
public class Team extends AbstractAuditingEntity implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    private boolean active;

    @NotNull
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    @Pattern(regexp = "[\\-A-Za-z0-9 '=_;:`@#&,.!()/]*", message = "Name can only contain letters, digits, spaces, and the following characters: - ' = _ ; : @ # & , . ! ( ) /")
    private String name;
    
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @NotNull
    @Size(max = 255)
    @Column(name="normalized_team_name", length = 255, nullable = false)
    @NotAudited
    @Pattern(regexp = "[a-z0-9 ]*", message = "Normalized name can only contain lowercase letters, digits, and spaces")
    private String normalizedTeamName;    
 

	public String getNormalizedTeamName() {
		return normalizedTeamName;
	}

	public void setNormalizedTeamName(String normalizedTeamName) {
		this.normalizedTeamName = normalizedTeamName;
	}

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "salesforce_account_id", nullable = false)
    @JsonManagedReference
    private TeamSalesForce salesForceAccount;   
    
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public TeamSalesForce getSalesForceAccount() {
		return salesForceAccount;
	}

	public void setSalesForceAccount(TeamSalesForce teamSalesForceAccount) {
		this.salesForceAccount = teamSalesForceAccount;
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
		Team other = (Team) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", version=" + version + ", name=" + name
				+ "]";
	}
	
}
