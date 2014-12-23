package com.emmisolutions.emmimanager.model.user.client.team;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Link between a client users and team roles for a specific team. Think of this
 * as a ternary object joining user, role and team.
 */
@Audited
@Entity
@Table(name = "user_client_user_client_team_role", uniqueConstraints = @UniqueConstraint(columnNames = {
        "users_id", "client_team_id", "user_client_team_role_id" }, name = "uk_user_client_user_client_team_role"))
public class UserClientUserClientTeamRole extends AbstractAuditingEntity {

    /**
     * Default constructor
     */
    public UserClientUserClientTeamRole() {

    }

    /**
     * Constructor with id
     * 
     * @param id
     */
    public UserClientUserClientTeamRole(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_client_team_role_id", nullable = false, columnDefinition = "bigint", foreignKey = @ForeignKey(name = "fk_user_client_user_client_team_role_role"))
    private UserClientTeamRole userClientTeamRole;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_team_id", nullable = false, columnDefinition = "bigint", foreignKey = @ForeignKey(name = "fk_user_client_client_team"))
    private Team team;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "users_id", nullable = false, columnDefinition = "bigint", foreignKey = @ForeignKey(name = "fk_user_client_user_client_team_role_users"))
    private UserClient userClient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserClientTeamRole getUserClientTeamRole() {
        return userClientTeamRole;
    }

    public void setUserClientTeamRole(UserClientTeamRole userClientTeamRole) {
        this.userClientTeamRole = userClientTeamRole;
    }

    public UserClient getUserClient() {
        return userClient;
    }

    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserClientUserClientTeamRole that = (UserClientUserClientTeamRole) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientUserClientTeamRole{" + "id=" + id
                + ", userClientTeamRole=" + userClientTeamRole + ", team="
                + team + ", userClient=" + userClient + '}';
    }
}
