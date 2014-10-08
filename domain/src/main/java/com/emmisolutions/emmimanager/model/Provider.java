package com.emmisolutions.emmimanager.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;

@Audited
@Table(name = "provider")
@XmlRootElement(name = "provider")
@Entity
public class Provider extends AbstractAuditingEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;

	private boolean active;

	@NotNull
	@Size(max = 255)
	@Column(name = "first_name", nullable = false)
	@Pattern(regexp = "[A-Za-z '-]*", message = "Name can only contain letters, spaces, and the following characters: - '")
	private String firstName;

	@NotNull
	@Size(max = 255)
	@Column(name = "last_name", nullable = false)
	@Pattern(regexp = "[A-Za-z '-]*", message = "Name can only contain letters, spaces, and the following characters: - '")
	private String lastName;

	@NotNull
	@Size(max = 255)
	@Column(name = "middle_name", nullable = false)
	@Pattern(regexp = "[A-Za-z '-]*", message = "Name can only contain letters, spaces, and the following characters: - '")
	private String middleName;

	@Email
	@NotNull
	@Size(min = 0, max = 100)
	@Column(length = 100, nullable = false)
	private String email;

	@Column(length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;
/*	
	@Column(length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;*/

	@ManyToMany
	@JoinTable (name="provider_team",
	joinColumns={@JoinColumn(name="provider_id", referencedColumnName="id")},
	inverseJoinColumns = {@JoinColumn(name="teams_id", referencedColumnName="id")})
	private Set<Team> teams;

	public Set<Team> getTeams() {
		return teams;
	}

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
		Provider other = (Provider) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Provider [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", middleName=" + middleName
				+ ", email=" + email + "]";
	}

}
