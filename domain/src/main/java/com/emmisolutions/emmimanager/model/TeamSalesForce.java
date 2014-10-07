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
 * A Team SalesForce account.
 */
@Audited
@Entity
@Table(name = "salesforce_team",
        uniqueConstraints = @UniqueConstraint(name = "uk_salesforce_team_account_number", columnNames = "account_number"))
@XmlRootElement(name = "salesforce")
public class TeamSalesForce extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]{18}")
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    private String name;
    private String street;
    private String city;
    private String state;
    @Column(name = "postal_code")
    private String postalCode;
    private String country;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "fax_number")
    @XmlElement(name = "fax")
    private String faxNumber;
    
    @OneToOne(mappedBy = "salesForceAccount")
    @JsonBackReference
    private Team team ;
    
    public TeamSalesForce() {
    }

    /**
     * Constructor
     * @param accountNumber required field
     */
    public TeamSalesForce(String accountNumber) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamSalesForce that = (TeamSalesForce) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TeamSalesForce{" +
                "accountNumber='" + accountNumber + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}
