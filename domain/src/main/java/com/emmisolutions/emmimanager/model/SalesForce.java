package com.emmisolutions.emmimanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * A SalesForce account.
 */
@Audited
@Entity
@Table(name = "salesforce_client",
        uniqueConstraints = @UniqueConstraint(name = "uk_account_number", columnNames = "account_number"))
@XmlRootElement(name = "salesforce")
public class SalesForce extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]{18}")
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @OneToOne(mappedBy = "salesForceAccount")
    @JsonBackReference
    private Client client;

    private String name;
    private String street;
    private String city;
    private String state;
    @Column(name = "postal_code")
    private String postalCode;
    private String country;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Transient
    private String faxNumber;
    
    public SalesForce() {
    }

    /**
     * Constructor
     * @param accountNumber required field
     */
    public SalesForce(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesForce that = (SalesForce) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{" +
                "\"accountNumber\"=\"" + accountNumber + "\"" +
                ", \"name\"=\"" + name + "\"" +
                ", \"id\"=" + id +
                '}';
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Helper for serialization
     * @return the client name if a client exists
     */
    @XmlElement
    public String getClientName() {
        return (client != null) ? client.getName() : null;
    }

    @XmlElement(name = "fax")
	public String getFax() {
		return faxNumber;
	}

	public void setFax(String fax) {
		this.faxNumber = fax;
	}

}
