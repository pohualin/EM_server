package com.emmisolutions.emmimanager.model.user.client.team;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * A team level permission
 */
@Entity
@Table(name = "user_client_team_permission")
@XmlRootElement(name = "permission")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientTeamPermission {

    @Id
    @Column(length = 100, columnDefinition = "varchar(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserClientTeamPermissionName name;

    @Column(name ="rank", columnDefinition = "int", nullable = false)
    private int rank;

    public UserClientTeamPermission() {
    }

    /**
     * Make a permission by name
     * @param name to use
     */
    public UserClientTeamPermission(UserClientTeamPermissionName name) {
        this.name = name;
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
        UserClientTeamPermission that = (UserClientTeamPermission) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientTeamPermission{" +
            "name=" + name +
            ", rank=" + rank +
            '}';
    }

    @ManyToMany(mappedBy = "userClientTeamPermissions")
    private Collection<UserClientTeamRole> userClientTeamRoles;

    public Collection<UserClientTeamRole> getUserClientTeamRoles() {
        return userClientTeamRoles;
    }

    public void setUserClientTeamRoles(Collection<UserClientTeamRole> userClientTeamRoles) {
        this.userClientTeamRoles = userClientTeamRoles;
    }
}
