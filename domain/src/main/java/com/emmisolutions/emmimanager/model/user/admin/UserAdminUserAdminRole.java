package com.emmisolutions.emmimanager.model.user.admin;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Link between an admin user and admin roles.
 */
@Audited
@Entity
@Table(name = "user_admin_user_admin_role",
    uniqueConstraints =
    @UniqueConstraint(columnNames = {"users_id", "user_admin_role_id"},
        name = "uk_user_admin_user_admin_role")
)
public class UserAdminUserAdminRole extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_admin_role_id", nullable = false, columnDefinition = "bigint",
        foreignKey = @ForeignKey(name = "fk_user_admin_user_admin_role_role")
    )
    private UserAdminRole userAdminRole;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "users_id", nullable = false, columnDefinition = "bigint",
        foreignKey = @ForeignKey(name = "fk_user_admin_user_admin_users")
    )
    private UserAdmin userAdmin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAdminRole getUserAdminRole() {
        return userAdminRole;
    }

    public void setUserAdminRole(UserAdminRole userAdminRole) {
        this.userAdminRole = userAdminRole;
    }

    public UserAdmin getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(UserAdmin userAdmin) {
        this.userAdmin = userAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAdminUserAdminRole that = (UserAdminUserAdminRole) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserAdminUserAdminRole{" +
            "id=" + id +
            ", userAdminRole=" + userAdminRole +
            ", userAdmin=" + userAdmin +
            '}';
    }
}
