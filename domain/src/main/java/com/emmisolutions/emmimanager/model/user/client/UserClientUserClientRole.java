package com.emmisolutions.emmimanager.model.user.client;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Link between a client users and client roles. Aka the client roles for a
 * client user.
 */
@Audited
@Entity
@Table(name = "user_client_user_client_role", uniqueConstraints = @UniqueConstraint(columnNames = {
        "users_id", "user_client_role_id" }, name = "uk_user_client_user_client_role"))
public class UserClientUserClientRole extends AbstractAuditingEntity {

    public UserClientUserClientRole() {

    }

    public UserClientUserClientRole(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_client_role_id", nullable = false, columnDefinition = "bigint", foreignKey = @ForeignKey(name = "fk_user_client_user_client_role_role"))
    private UserClientRole userClientRole;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "users_id", nullable = false, columnDefinition = "bigint", foreignKey = @ForeignKey(name = "fk_user_client_user_client_role_users"))
    private UserClient userClient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserClientRole getUserClientRole() {
        return userClientRole;
    }

    public void setUserClientRole(UserClientRole userClientRole) {
        this.userClientRole = userClientRole;
    }

    public UserClient getUserClient() {
        return userClient;
    }

    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserClientUserClientRole that = (UserClientUserClientRole) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientUserClientRole{" + "id=" + id + ", userClientRole="
                + userClientRole + ", userClient=" + userClient + '}';
    }
}
