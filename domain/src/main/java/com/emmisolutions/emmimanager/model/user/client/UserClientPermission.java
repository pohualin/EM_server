package com.emmisolutions.emmimanager.model.user.client;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * A client level permission
 */
@Entity
@Table(name = "user_client_permission")
@XmlRootElement(name = "permission")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientPermission {

    @Id
    @Column(length = 100, columnDefinition = "varchar(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserClientPermissionName name;

    @Column(name ="rank", columnDefinition = "int", nullable = false)
    private int rank;

    public UserClientPermission() {
    }

    /**
     * Make a permission by name
     * @param name to use
     */
    public UserClientPermission(UserClientPermissionName name) {
        this.name = name;
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
        UserClientPermission that = (UserClientPermission) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientPermission{" +
            "name=" + name +
            ", rank=" + rank +
            '}';
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @ManyToMany(mappedBy = "userClientPermissions")
    private Collection<UserClientRole> userClientRoles;

    public Collection<UserClientRole> getUserClientRoles() {
        return userClientRoles;
    }

    public void setUserClientRoles(Collection<UserClientRole> userClientRoles) {
        this.userClientRoles = userClientRoles;
    }
}
