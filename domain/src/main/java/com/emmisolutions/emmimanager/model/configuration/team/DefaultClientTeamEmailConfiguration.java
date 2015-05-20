package com.emmisolutions.emmimanager.model.configuration.team;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;

/**
 * The default team email configuration.
 */
@Entity
@Table(name = "default_team_email_configuration")
@XmlRootElement(name = "default_team_email_configuration")
public class DefaultClientTeamEmailConfiguration extends AbstractAuditingEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Version
    private Integer version;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @NotNull
    @Column(columnDefinition = "nvarchar(255)", nullable = false)
    private String description;
    
    @NotNull
    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    private String type;
    
    @Column(name ="rank", columnDefinition = "integer")
	private Integer rank;

	@Column(name = "default_value", columnDefinition = "boolean", nullable = false)
    private boolean defaultValue;

    
    /**
     * Default constructor
     */
    public DefaultClientTeamEmailConfiguration() {

    }

    /**
     * ID constructor
     *
     * @param id
     *            to use
     */
    public DefaultClientTeamEmailConfiguration(Long id) {
        this.id = id;
    }
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank; 
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
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DefaultClientTeamEmailConfiguration that = (DefaultClientTeamEmailConfiguration) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }
	
	@Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
	
	@Override
    public String toString() {
        return "DefaultClientTeamEmailConfiguration{" + "id=" + id
                + ", type=" + type +
                ", description=" + description + " ,defaultValue=" +
                defaultValue +  '}';
    }

      
}
