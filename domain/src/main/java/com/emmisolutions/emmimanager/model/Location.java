package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client belongsTo;

    @ManyToMany(mappedBy = "locations")
    private Set<Client> usingThisLocation;

    @NotNull
    @Pattern(regexp = "[A-Za-z-'=_;:@#&,.!() ]*", message = "Can only contain letters, digits, spaces, and the following characters: - ' = _ ; : @ # & , . ! ( )")
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(length = 20, nullable = false)
    private String phone;

    @NotNull
    @Pattern(regexp = "[A-Za-z-'=_;:@#&,.!() ]*", message = "Can only contain letters, digits, spaces, and the following characters: - ' = _ ; : @ # & , . ! ( )")
    @Column(nullable = false)
    private String city;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 2, nullable = false)
    private State state;

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

    public Set<Client> getUsingThisLocation() {
        return usingThisLocation;
    }

    public void setUsingThisLocation(Set<Client> usingThisLocation) {
        this.usingThisLocation = usingThisLocation;
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
}
