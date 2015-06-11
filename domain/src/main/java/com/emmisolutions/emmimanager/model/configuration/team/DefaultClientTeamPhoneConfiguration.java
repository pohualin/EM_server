package com.emmisolutions.emmimanager.model.configuration.team;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
//import com.emmisolutions.emmimanager.model.PhoneReminderType;

/**
 * The default team phone configuration.
 */
@Entity
@Table(name = "default_team_phone_configuration")
@XmlRootElement(name = "default_team_phone_configuration")
public class DefaultClientTeamPhoneConfiguration extends AbstractAuditingEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Version
    private Integer version;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "collect_phone", columnDefinition = "boolean", nullable = false)
    private boolean collectPhone;
    
    @Column(name = "require_phone", columnDefinition = "boolean", nullable = false)
    private boolean requirePhone;
     
    
    /**
     * Default constructor
     */
    public DefaultClientTeamPhoneConfiguration() {

    }

    /**
     * ID constructor
     *
     * @param id
     *            to use
     */
    public DefaultClientTeamPhoneConfiguration(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
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

	public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DefaultClientTeamPhoneConfiguration that = (DefaultClientTeamPhoneConfiguration) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }
	
	@Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
	
	@Override
    public String toString() {
        return "DefaultClientTeamEmailConfiguration{" + "id=" + id
                + ", collect_phone=" + collectPhone 
                + ", require_phone=" + requirePhone
                +  '}';
    }

      
}
