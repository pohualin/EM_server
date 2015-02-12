package com.emmisolutions.emmimanager.model.user.client.reference;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * A client level reference permission.
 */
@Audited
@Entity
@Table(name = "user_client_reference_role_permission")
public class UserClientReferenceRolePermission extends AbstractAuditingEntity implements
        Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @ManyToOne
    @JoinColumn(name="name", columnDefinition = "varchar(100)", nullable = false)
    @NotNull
    @Audited(targetAuditMode = NOT_AUDITED)
    private UserClientPermission permission;

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
     * @param permission to use
     */
    public UserClientReferenceRolePermission(UserClientPermission permission) {
        this.setPermission(permission);
    }

    public UserClientReferenceRole getUserClientReferenceRole() {
        return userClientReferenceRole;
    }

    public void setUserClientReferenceRole(UserClientReferenceRole userClientReferenceRole) {
        this.userClientReferenceRole = userClientReferenceRole;
    }

    public UserClientPermission getPermission() {
        return permission;
    }

    public void setPermission(UserClientPermission name) {
        this.permission = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClientReferenceRolePermission that = (UserClientReferenceRolePermission) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientReferenceRolePermission{" +
            "id=" + id +
            ", permission=" + permission +
            ", userClientReferenceRole=" + userClientReferenceRole +
            '}';
    }
}
