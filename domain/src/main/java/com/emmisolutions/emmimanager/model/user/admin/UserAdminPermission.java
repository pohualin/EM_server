package com.emmisolutions.emmimanager.model.user.admin;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This is an application permission.
 */
@Entity
@Table(name = "user_admin_permission")
public class UserAdminPermission {

    @Id
    @NotNull
    @Size(min = 0, max = 100)
    @Column(length = 100, columnDefinition = "varchar(100)")
    @Enumerated(EnumType.STRING)
    private UserAdminPermissionName name;

    public UserAdminPermissionName getName() {
        return name;
    }

    public void setName(UserAdminPermissionName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAdminPermission that = (UserAdminPermission) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserAdminPermission{" +
                "name=" + name +
                '}';
    }
}
