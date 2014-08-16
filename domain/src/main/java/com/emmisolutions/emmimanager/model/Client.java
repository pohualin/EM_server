package com.emmisolutions.emmimanager.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A client.
 */
@Audited
@Entity
@Table(name = "client", uniqueConstraints = @UniqueConstraint(name = "uk_salesforce_account_id", columnNames = "salesforce_account_id"))
@XmlRootElement(name = "client")
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

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "salesforce_account_id")
    @JsonManagedReference
    private SalesForce salesForceAccount;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client", orphanRemoval = true)
    @JsonManagedReference
    @XmlElement(name = "group")
    @XmlElementWrapper(name = "groups")
    @JsonProperty("group")
    private Set<Group> groups = new HashSet<>();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return !(id != null ? !id.equals(client.id) : client.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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

	public void addGroup(Group group){
		group.setClient(this);
		this.getGroups().add(group);		
	}

}
