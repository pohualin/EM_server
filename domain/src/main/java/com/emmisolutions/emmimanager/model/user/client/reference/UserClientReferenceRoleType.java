package com.emmisolutions.emmimanager.model.user.client.reference;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

/**
 * Used to allow reference roles to be renamed but still maintain their original purpose.
 */
@Audited
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "user_client_reference_role_type")
public class UserClientReferenceRoleType extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String name;

    public UserClientReferenceRoleType() {
    }

    /**
     * Make a type by id
     * @param id the id
     */
    public UserClientReferenceRoleType(Long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClientReferenceRoleType that = (UserClientReferenceRoleType) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserClientReferenceRoleType{" +
            "id=" + id +
            ", version=" + version +
            ", name='" + name + '\'' +
            '}';
    }
}
