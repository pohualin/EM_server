package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Client Types
 * <p/>
 * PROVIDER, PHYSICIAN_PRACTICE, PAYER, PHARMA, OTHER
 */
@Audited
@Entity
@Table(name = "client_type", uniqueConstraints = @UniqueConstraint(name = "uk_client_type_key_path", columnNames = "key_path"))
@XmlRootElement(name = "client_type")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientType {

    public ClientType() {
    }

    /**
     * Primary key constructor
     *
     * @param id the id
     */
    public ClientType(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_path", length = 1024, nullable = false)
    private String typeKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientType that = (ClientType) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
