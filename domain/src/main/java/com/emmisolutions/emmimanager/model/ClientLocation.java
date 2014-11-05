package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Location used by a Client
 */
@Audited
@Entity
@Table(name = "client_location",
        uniqueConstraints = @UniqueConstraint(name = "uk_client_location",
                columnNames = {"client_id", "location_id"}))
@XmlRootElement(name = "client_location")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientLocation extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_client_client_id"))
    private Client client;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_location_location_id"))
    private Location location;

    /**
     * No arg constructor
     */
    public ClientLocation() {

    }

    /**
     * Create ClientLocation by id
     *
     * @param id to use
     */
    public ClientLocation(Long id) {
        this.id = id;
    }

    /**
     * Create with the composite parts
     *
     * @param client   to use
     * @param location to use
     */
    public ClientLocation(Client client, Location location) {
        this.client = client;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientLocation that = (ClientLocation) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientLocation{" +
                "id=" + id +
                ", client=" + client +
                ", location=" + location +
                '}';
    }
}
