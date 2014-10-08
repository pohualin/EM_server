package com.emmisolutions.emmimanager.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * A client.
 */
@Audited
@Entity
@Table(name = "client", uniqueConstraints = @UniqueConstraint(name = "uk_salesforce_account_id", columnNames = "salesforce_account_id"))
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
    @Pattern(regexp = "[\\-A-Za-z0-9 '=_;:`@#&,.!()/]*", message = "Name can only contain letters, digits, spaces, and the following characters: - ' = _ ; : @ # & , . ! ( ) /")
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_type_id", nullable = false)
    private ClientType type;

    @ManyToOne
    @JoinColumn(name = "client_region_id")
    private ClientRegion region;

    @ManyToOne
    @JoinColumn(name = "client_tier_id")
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

    @NotNull
    @Size(max = 255)
    @Column(name="normalized_name", length = 255, nullable = false)
    @NotAudited
    @Pattern(regexp = "[a-z0-9]*", message = "Normalized name can only contain lowercase letters, digits, and spaces")
    private String normalizedName;    
 
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

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }
    
    @AssertTrue(message = "Contract End Date must be at least one day after the Contract Start Date")
    private boolean isValid() {
        return contractStart != null && contractEnd != null &&
                (contractStart.plus(Days.days(1)).isEqual(contractEnd) || contractStart.plus(Days.days(1)).isBefore(contractEnd));
    }
}
