package com.emmisolutions.emmimanager.model;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A patient.
 */
@Table(name = "client_patient")
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
    @Pattern(regexp = "[A-Za-z '-]+\\.{0,1}[A-Za-z '-]*", message = "Name can only contain letters, spaces, and the following characters: . - '")
    private String firstName;

    @NotNull
    @Size(max = 255)
    @Column(name = "last_name", nullable = false)
    @Pattern(regexp = "[A-Za-z '-]+\\.{0,1}[A-Za-z '-]*", message = "Name can only contain letters, spaces, and the following characters: . - '")
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

    @Email
    @Size(min = 0, max = 255)
    @Column(columnDefinition = "nvarchar(255)")
    private String email;

    @Column(name = "phone", length = 12, columnDefinition = "nvarchar(12)")
    @Pattern(regexp = "\\s*|[2-9][0-9][0-9]-[2-9][0-9][0-9]-[0-9][0-9][0-9][0-9]",
            message = "Phone number must be 10 digits and the 1st and 4th digit cannot be a 1 or 0")
    private String phone;

    @NotNull
    @Size(max = 255)
    @Column(name = "normalized_name", length = 510, nullable = false)
    @NotAudited
    @Pattern(regexp = "[a-z0-9]*", message = "Normalized name can only contain lowercase letters, digits, and spaces")
    private String normalizedName;

    @OneToMany(mappedBy = "patient")
    @XmlElement(name = "scheduledProgram")
    @XmlElementWrapper(name = "scheduledPrograms")
    private Set<ScheduledProgram> scheduledPrograms;

    @ManyToOne
    @JoinColumn(name = "patient_opt_out_preference")
    private PatientOptOutPreference optOutPreference;

    /**
     * No arg constructor
     */
    public Patient() {
    }

    /**
     * Creates a patient by id
     *
     * @param id the id
     */
    public Patient(Long id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<ScheduledProgram> getScheduledPrograms() {
        return scheduledPrograms;
    }

    public void setScheduledPrograms(Set<ScheduledProgram> scheduledPrograms) {
        this.scheduledPrograms = scheduledPrograms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public PatientOptOutPreference getOptOutPreference() {
        return optOutPreference;
    }

    public void setOptOutPreference(PatientOptOutPreference optOutPreference) {
        this.optOutPreference = optOutPreference;
    }
}
