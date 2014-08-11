package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A client.
 */
@Audited
@Entity
@Table(name = "client")
@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
public class Client extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    private boolean active;

    @NotNull
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private ClientType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ClientRegion region;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "client_location",
            joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "location_id", referencedColumnName = "id")})
    private Set<Location> locations = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ClientTier tier;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_owner_id")
    private User contractOwner;

    @NotNull
    @Column(name = "contract_start", nullable = false)
    private LocalDate contractStart;

    @NotNull
    @Column(name = "contract_end", nullable = false)
    private LocalDate contractEnd;

    @OneToOne(mappedBy = "client")
    @JoinColumn(name = "salesforce_account_id")
    private SalesForce salesForceAccount;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "client_tag_group",
            joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")})
    private Set<Group> groups = new HashSet<>();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return !(getId() != null ? !getId().equals(client.getId()) : client.getId() != null) && !(getVersion() != null ? !getVersion().equals(client.getVersion()) : client.getVersion() != null);
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() +
                ", version=" + getVersion() +
                ", name='" + getName() + '\'' +
                '}';
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public ClientRegion getRegion() {
        return region;
    }

    public void setRegion(ClientRegion region) {
        this.region = region;
    }

    public ClientTier getTier() {
        return tier;
    }

    public void setTier(ClientTier tier) {
        this.tier = tier;
    }

    public User getContractOwner() {
        return contractOwner;
    }

    public void setContractOwner(User contractOwner) {
        this.contractOwner = contractOwner;
    }

    public LocalDate getContractStart() {
        return contractStart;
    }

    public void setContractStart(LocalDate contractStart) {
        this.contractStart = contractStart;
    }

    public LocalDate getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(LocalDate contractEnd) {
        this.contractEnd = contractEnd;
    }

    public SalesForce getSalesForceAccount() {
        return salesForceAccount;
    }

    public void setSalesForceAccount(SalesForce salesForceAccount) {
        this.salesForceAccount = salesForceAccount;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
}
