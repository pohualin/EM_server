package com.emmisolutions.emmimanager.model.user.client;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Client;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * A role defined for a specific Client.
 */
@Entity
@Audited
@Table(name = "user_client_role",
    uniqueConstraints =
    @UniqueConstraint(columnNames = {"client_id","name"}, name = "uk_user_client_role_name"))
public class UserClientRole extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @NotNull
    @Column(length = 255, columnDefinition = "nvarchar(255)", nullable = false)
    @Size(min = 0, max = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_client_role_client")
    )
    private Client client;

    @ManyToMany
    @JoinTable(
        name = "user_client_role_user_client_permission",
        joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "fk_user_client_role_user_client_permission_role"))
        },
        inverseJoinColumns = {
            @JoinColumn(name = "permission_name", referencedColumnName = "name",
                foreignKey = @ForeignKey(name = "fk_user_client_role_user_client_permission_permission"))
        }
    )
    @Audited(targetAuditMode = NOT_AUDITED)
    private Set<UserClientPermission> userClientPermissions;

    @Version
    @Column(columnDefinition = "int")
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<UserClientPermission> getUserClientPermissions() {
        return userClientPermissions;
    }

    public void setUserClientPermissions(Set<UserClientPermission> userClientPermissions) {
        this.userClientPermissions = userClientPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClientRole role = (UserClientRole) o;
        return !(id != null ? !id.equals(role.id) : role.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientRole{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

}
