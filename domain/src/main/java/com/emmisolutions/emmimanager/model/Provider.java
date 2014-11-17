package com.emmisolutions.emmimanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
/**
 * A Provider and its specialty and teams
 */
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

	@Size(max = 255)
	@Column(name = "middle_name")
	@Pattern(regexp = "[A-Za-z '-]*", message = "Name can only contain letters, spaces, and the following characters: - '")
	private String middleName;

	@Email
	@Size(min = 0, max = 100)
	@Column(length = 100)
	private String email;

	@ManyToOne
	@JoinColumn(name = "reference_tag_specialty")
	private ReferenceTag specialty;
	
	@NotNull
    @Size(max = 512)
    @Column(name="normalized_name", length = 512, nullable = false)
    @NotAudited
    @Pattern(regexp = "[a-z0-9]*", message = "Normalized name can only contain lowercase letters, digits, and spaces")
    private String normalizedName;
	
	public Provider(){
		
	}
	
	/**
     * ID constructor
     *
     * @param id      to use
     */
    public Provider(Long id) {
        this.id = id;
    }

	public String getNormalizedName() {
		return normalizedName;
	}

	public void setNormalizedName(String normalizedName) {
		this.normalizedName = normalizedName;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ReferenceTag getSpecialty() {
		return specialty;
	}

	public void setSpecialty(ReferenceTag specialty) {
		this.specialty = specialty;
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
