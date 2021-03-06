package com.emmisolutions.emmimanager.model.user.client.team.reference;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Team Level Reference Role.
 */
@Audited
@Entity
@XmlRootElement(name = "reference_role")
@Table(name = "user_client_reference_team_role")
public class UserClientReferenceTeamRole extends AbstractAuditingEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(max = 255)
	@Column(length = 255, nullable = false, columnDefinition = "nvarchar(255)")
	private String name;
	
	@NotNull
    @Size(max = 255)
    @Column(name="normalized_name", length = 255, nullable = false, columnDefinition = "nvarchar(255)")
	@NotAudited
    private String normalizedName;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "userClientReferenceTeamRole")
	@XmlElement(name = "permission")
	@XmlElementWrapper(name = "permissions")
	@JsonProperty("permission")
	private Set<UserClientReferenceTeamRolePermission> permissions = new HashSet<>();

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name ="reference_role_type_id", nullable = false)
    private UserClientReferenceTeamRoleType type;

    @Version
    private Integer version;

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

	public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public UserClientReferenceTeamRoleType getType() {
		return type;
	}

	public void setType(UserClientReferenceTeamRoleType type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserClientReferenceTeamRole group = (UserClientReferenceTeamRole) o;
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
		return "UserClientReferenceTeamRole{" + "id=" + getId() + ", name='" + getName() + '\'' + '}';
	}

    public Set<UserClientReferenceTeamRolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<UserClientReferenceTeamRolePermission> permissions) {
        this.permissions = permissions;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
