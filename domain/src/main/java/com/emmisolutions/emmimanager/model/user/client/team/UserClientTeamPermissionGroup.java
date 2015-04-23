package com.emmisolutions.emmimanager.model.user.client.team;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A client team level permission group
 */
@Entity
@Table(name = "user_client_team_permission_group")
@XmlRootElement(name = "client_team_permission_group")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientTeamPermissionGroup implements Serializable {

    @Id
    @Column(name = "group_name", length = 100, columnDefinition = "nvarchar(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserClientTeamPermissionGroupName name;

    @Column(name = "rank", columnDefinition = "int", nullable = false)
    private int rank;

    public UserClientTeamPermissionGroup() {
    }

    /**
     * Make a permission by name
     * 
     * @param name
     *            to use
     */
    public UserClientTeamPermissionGroup(UserClientTeamPermissionGroupName name) {
        this.name = name;
    }

    public UserClientTeamPermissionGroupName getName() {
        return name;
    }

    public void setName(UserClientTeamPermissionGroupName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserClientTeamPermissionGroup that = (UserClientTeamPermissionGroup) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientTeamPermissionGroup{" + "name=" + name + ", rank="
                + rank + '}';
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
