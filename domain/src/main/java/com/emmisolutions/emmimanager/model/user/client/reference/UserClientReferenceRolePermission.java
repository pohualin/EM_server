package com.emmisolutions.emmimanager.model.user.client.reference;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A client level reference permission.
 */
@Audited
@Entity
@Table(name = "user_client_reference_role_permission")
public class UserClientReferenceRolePermission extends AbstractAuditingEntity implements
		Serializable {

    @Id
    @Column(length = 100, columnDefinition = "varchar(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserClientPermissionName name;

	@ManyToOne
	@JoinColumn(name = "reference_role_id")
	@JsonBackReference
	@NotNull
	private UserClientReferenceRole userClientReferenceRole;

    /**
     * No arg constructor
     */
    public UserClientReferenceRolePermission() {
    }

    /**
     * Make a permission by name
     * @param name to use
     */
    public UserClientReferenceRolePermission(UserClientPermissionName name) {
        this.setName(name);
    }

	public UserClientReferenceRole getUserClientReferenceRole() {
		return userClientReferenceRole;
	}

	public void setUserClientReferenceRole(UserClientReferenceRole userClientReferenceRole) {
		this.userClientReferenceRole = userClientReferenceRole;
	}

    public UserClientPermissionName getName() {
        return name;
    }

    public void setName(UserClientPermissionName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClientReferenceRolePermission that = (UserClientReferenceRolePermission) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
