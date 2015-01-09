package com.emmisolutions.emmimanager.model.user.admin;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;

/**
 * A role defined for administrative users.
 */
@Entity
@Audited
@Table(name = "user_admin_role",
    uniqueConstraints =
    @UniqueConstraint(columnNames = {"name"}, name = "uk_user_admin_role_name"))
@XmlRootElement(name = "role")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserAdminRole extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @NotNull
    @Column(length = 255, columnDefinition = "nvarchar(255)")
    @Size(min = 0, max = 255)
    private String name;

    @NotNull
    @Column(name = "default_role", updatable = false, insertable = false, nullable = false)
    private boolean defaultRole;
    
    @NotNull
    @Column(name = "system_role", updatable = false, insertable = false, nullable = false)
    private boolean systemRole;

    @ManyToMany
    @JoinTable(
        name = "user_admin_role_user_admin_permission",
        joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "fk_user_admin_role_user_admin_permission_role"))
        },
        inverseJoinColumns = {
            @JoinColumn(name = "permission_name", referencedColumnName = "name",
                foreignKey = @ForeignKey(name = "fk_user_admin_role_user_admin_permission_permission"))
        }
    )
    @Audited(targetAuditMode = NOT_AUDITED)
    private Set<UserAdminPermission> permissions;

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

    public Set<UserAdminPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<UserAdminPermission> permissions) {
        this.permissions = permissions;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAdminRole role = (UserAdminRole) o;
        return !(id != null ? !id.equals(role.id) : role.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserAdminRole{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    public boolean isDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(boolean defaultRole) {
        this.defaultRole = defaultRole;
    }

    public boolean isSystemRole() {
        return systemRole;
    }

    public void setSystemRole(boolean systemRole) {
        this.systemRole = systemRole;
    }

}
