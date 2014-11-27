package com.emmisolutions.emmimanager.model.user.client.team.reference;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A team level reference permission.
 */
@Audited
@Entity
@Table(
    name = "user_client_reference_team_role_permission",
    uniqueConstraints =
    @UniqueConstraint(
        name = "uk_user_client_reference_team_role_permission",
        columnNames = {"name", "reference_role_id"}
    )
)
public class UserClientReferenceTeamRolePermission extends AbstractAuditingEntity implements
    Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(length = 100, columnDefinition = "varchar(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserClientTeamPermissionName name;

    @ManyToOne
    @JoinColumn(name = "reference_role_id")
    @JsonBackReference
    @NotNull
    private UserClientReferenceTeamRole userClientReferenceTeamRole;

    /**
     * No arg constructor
     */
    public UserClientReferenceTeamRolePermission() {
    }

    /**
     * Make a permission by name
     *
     * @param name to use
     */
    public UserClientReferenceTeamRolePermission(UserClientTeamPermissionName name) {
        this.setName(name);
    }

    public UserClientReferenceTeamRole getUserClientReferenceTeamRole() {
        return userClientReferenceTeamRole;
    }

    public void setUserClientReferenceTeamRole(UserClientReferenceTeamRole userClientReferenceTeamRole) {
        this.userClientReferenceTeamRole = userClientReferenceTeamRole;
    }

    public UserClientTeamPermissionName getName() {
        return name;
    }

    public void setName(UserClientTeamPermissionName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClientReferenceTeamRolePermission that = (UserClientReferenceTeamRolePermission) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientReferenceTeamRolePermission{" +
            "name=" + name +
            ", userClientReferenceTeamRole=" + userClientReferenceTeamRole +
            '}';
    }
}
