package com.emmisolutions.emmimanager.model.user.client.team.reference;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

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

    @ManyToOne
    @JoinColumn(name="name", columnDefinition = "varchar(100)", nullable = false)
    @NotNull
    @Audited(targetAuditMode = NOT_AUDITED)
    private UserClientTeamPermission permission;

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
     * @param permission to use
     */
    public UserClientReferenceTeamRolePermission(UserClientTeamPermission permission) {
        this.setPermission(permission);
    }

    public UserClientReferenceTeamRole getUserClientReferenceTeamRole() {
        return userClientReferenceTeamRole;
    }

    public void setUserClientReferenceTeamRole(UserClientReferenceTeamRole userClientReferenceTeamRole) {
        this.userClientReferenceTeamRole = userClientReferenceTeamRole;
    }

    public UserClientTeamPermission getPermission() {
        return permission;
    }

    public void setPermission(UserClientTeamPermission name) {
        this.permission = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClientReferenceTeamRolePermission that = (UserClientReferenceTeamRolePermission) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientReferenceTeamRolePermission{" +
            "id=" + id +
            ", permission=" + permission +
            ", userClientReferenceTeamRole=" + userClientReferenceTeamRole +
            '}';
    }
}
