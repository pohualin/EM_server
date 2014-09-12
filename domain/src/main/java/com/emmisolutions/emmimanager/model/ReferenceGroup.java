package com.emmisolutions.emmimanager.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Reference Group and its containing tags
 */
@Audited
@XmlRootElement(name = "reference_group")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "reference_group")
public class ReferenceGroup extends AbstractAuditingEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(max = 255)
	@Column(length = 255, nullable = false)
	private String name;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "group")
	@XmlElement(name = "tag")
	@XmlElementWrapper(name = "tags")
	@JsonProperty("tag")
	private Set<ReferenceTag> tags = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ReferenceTag> getTags() {
		return tags;
	}

	public void setTags(Set<ReferenceTag> tags) {
		this.tags = tags;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ReferenceGroup group = (ReferenceGroup) o;
		return !(getId() != null ? !getId().equals(group.getId()) : group
				.getId() != null);
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result;
		return result;
	}

	@Override
	public String toString() {
		return "Reference Group{" + "id=" + getId() + ", name='" + getName() + '\'' + '}';
	}

}
