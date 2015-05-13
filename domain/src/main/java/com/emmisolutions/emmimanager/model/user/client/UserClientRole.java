package com.emmisolutions.emmimanager.model.user.client;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRoleType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

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
    @UniqueConstraint(columnNames = {"client_id", "normalized_name"}, name = "uk_user_client_role_normalized_name"))
public class UserClientRole extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @NotNull
    @Column(length = 255, columnDefinition = "nvarchar(255)", nullable = false)
    @Size(min = 0, max = 255)
    @Pattern(regexp = "[\\-A-Za-z0-9 '=_;:`@#&,.!()/]*", message = "Name can only contain letters, digits, spaces, and the following characters: - ' = _ ; : @ # & , . ! ( ) /")
    private String name;
    
    @NotNull
    @Column(name = "normalized_name", length = 255, columnDefinition = "nvarchar(255)", nullable = false)
    @NotAudited
    @Size(min = 0, max = 255)
    private String normalizedName;

    @ManyToOne
    @JoinColumn(name = "user_client_reference_role_type_id", columnDefinition = "bigint")
    private UserClientReferenceRoleType type;

    public UserClientRole() {
    }

    /**
     * Create UserClientRole by id
     *
     * @param id to use
     */
    public UserClientRole(Long id) {
        this.id = id;
    }

    /**
     * Create UserClientRole by its parts
     *
     * @param name                  the name of the role
     * @param client                the client to use
     * @param userClientPermissions the permission set
     */
    public UserClientRole(String name, Client client, Set<UserClientPermission> userClientPermissions) {
        this.name = name;
        this.client = client;
        this.userClientPermissions = userClientPermissions;
    }

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

    @OneToMany(mappedBy = "userClientRole", cascade = CascadeType.REMOVE)
    @XmlTransient
    private Set<UserClientUserClientRole> userClientUserClientRoles;

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

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
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

    public Set<UserClientUserClientRole> getUserClientUserClientRoles() {
        return userClientUserClientRoles;
    }

    public void setUserClientUserClientRoles(Set<UserClientUserClientRole> userClientUserClientRoles) {
        this.userClientUserClientRoles = userClientUserClientRoles;
    }

    public UserClientReferenceRoleType getType() {
        return type;
    }

    public void setType(UserClientReferenceRoleType type) {
        this.type = type;
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
