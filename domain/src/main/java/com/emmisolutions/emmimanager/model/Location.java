package com.emmisolutions.emmimanager.model;

import java.util.Set;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A location.
 */
@Audited
@Entity
@Table(name = "location")
@XmlRootElement(name = "location")
@XmlAccessorType(XmlAccessType.FIELD)
public class Location extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client belongsTo;

    @NotNull
    @Length(max = 255)
    @Pattern(regexp = "[0-9A-Za-z-'=_;:@#&,.!() ]*", message = "Can only contain letters, digits, spaces, and the following characters: - ' = _ ; : @ # & , . ! ( )")
    @Column(nullable = false)
    private String name;

    @NotNull
    @Length(max = 255)
    @Pattern(regexp = "[2-9][0-9][0-9]-[2-9][0-9][0-9]-[0-9][0-9][0-9][0-9]", message = "Phone number must be 10 digits and the 1st and 4th digit cannot be a 1 or 0")
    @Column(nullable = false)
    private String phone;

    @NotNull
    @Length(max = 255)
    @Pattern(regexp = "[0-9A-Za-z-'=_;:@#&,.!() ]*", message = "Can only contain letters, digits, spaces, and the following characters: - ' = _ ; : @ # & , . ! ( )")
    @Column(nullable = false)
    private String city;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 2, nullable = false)
    private State state;

    @OneToMany(mappedBy="location")
    @XmlTransient
    private Set<ClientLocation> usingThisLocation;

    /**
     * No Arg Constructor
     */
    public Location() {

    }

    /**
     * JPA Id constructor
     *
     * @param id      to use
     * @param version the version
     */
    public Location(Long id, Integer version) {
        this.id = id;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Client getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(Client belongsTo) {
        this.belongsTo = belongsTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return !(id != null ? !id.equals(location.id) : location.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
