package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Client Regions
 * <p/>
 * OTHER, SOUTHEAST, NORTHEAST, MIDWEST, WEST
 */
@Audited
@Entity
@Table(name = "client_region", uniqueConstraints = @UniqueConstraint(name = "uk_client_region_key_path", columnNames = "key_path"))
@XmlRootElement(name = "client_region")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientRegion {

    public ClientRegion() {
    }

    /**
     * Primary key constructor
     *
     * @param id the id
     */
    public ClientRegion(Long id) {
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
        ClientRegion that = (ClientRegion) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientRegion{" +
                "id=" + id +
                ", typeKey='" + typeKey + '\'' +
                '}';
    }
}

