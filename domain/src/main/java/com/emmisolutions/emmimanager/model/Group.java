package com.emmisolutions.emmimanager.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

/**
 * A Group and its containing tags
 */
@Audited
@Entity
@Table(name = "tag_group")
@XmlRootElement(name = "tag_group")
@XmlAccessorType(XmlAccessType.FIELD)
public class Group  extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    private boolean active;

    @NotNull
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client belongsTo;

    @ManyToMany(mappedBy = "groups")
    private Set<Client> usingThisGroup;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return !(getId() != null ? !getId().equals(client.getId()) : client.getId() != null) && !(getVersion() != null ? !getVersion().equals(client.getVersion()) : client.getVersion() != null);
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + getId() +
                ", version=" + getVersion() +
                ", name='" + getName() + '\'' +
                '}';
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

	public Client getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(Client belongsTo) {
		this.belongsTo = belongsTo;
	}

	public Set<Client> getUsingThisGroup() {
		return usingThisGroup;
	}

	public void setUsingThisGroup(Set<Client> usingThisGroup) {
		this.usingThisGroup = usingThisGroup;
	}
}
