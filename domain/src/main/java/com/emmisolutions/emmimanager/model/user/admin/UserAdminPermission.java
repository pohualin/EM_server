package com.emmisolutions.emmimanager.model.user.admin;

import javax.persistence.*;

/**
 * This is an application permission.
 */
@Entity
@Table(name = "user_admin_permission")
public class UserAdminPermission {

    @Id
    @Column(length = 100, columnDefinition = "varchar(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserAdminPermissionName name;

    public UserAdminPermission() {
    }

    /**
     * Make a permission by name
     *
     * @param name to use
     */
    public UserAdminPermission(UserAdminPermissionName name) {
        this.name = name;
    }

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
