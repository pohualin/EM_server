package com.emmisolutions.emmimanager.model;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Client;
import org.aspectj.lang.annotation.Before;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * A patient.
 */
@Table(name = "patient")
@Audited
@XmlRootElement(name = "patient")
@Entity
public class Patient extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Size(max = 255)
    @Column(name = "first_name", nullable = false)
    @Pattern(regexp = "[A-Za-z '-]*", message = "Name can only contain letters, spaces, and the following characters: - '")
    private String firstName;

    @NotNull
    @Size(max = 255)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "date_of_birth")
    @DateTimeFormat
    private LocalDate dateOfBirth;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 25)
    private Gender gender;

    @NotNull
    @Size(max = 255)
    @Column(name = "normalized_name", length = 510, nullable = false)
    @NotAudited
    @Pattern(regexp = "[a-z0-9]*", message = "Normalized name can only contain lowercase letters, digits, and spaces")
    private String normalizedName;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        if (id != null ? !id.equals(patient.id) : patient.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
