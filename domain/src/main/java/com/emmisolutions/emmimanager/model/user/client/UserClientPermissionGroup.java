package com.emmisolutions.emmimanager.model.user.client;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * A client level permission group
 */
@Entity
@Table(name = "user_client_permission_group")
@XmlRootElement(name = "permission_group")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientPermissionGroup {

    @Id
    @Column(name="group_name", length = 100, columnDefinition = "varchar(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserClientPermissionGroupName name;
    
    @Column(name ="rank", columnDefinition = "int", nullable = false)
    private int rank;

    public UserClientPermissionGroup() {
    }

    /**
     * Make a permission by name
     * @param name to use
     */
    public UserClientPermissionGroup(UserClientPermissionGroupName name) {
        this.name = name;
    }

    public UserClientPermissionGroupName getName() {
        return name;
    }

    public void setName(UserClientPermissionGroupName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClientPermissionGroup that = (UserClientPermissionGroup) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientPermissionGroup{" +
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
