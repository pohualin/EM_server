package com.emmisolutions.emmimanager.model.schedule;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.user.User;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * A schedule for a patient who is receiving services from a provider on a team,
 * at a particular time, at a Location, for a program.
 */
@Entity
@Audited
@Table(name = "scheduled_program",
        indexes = {
                @Index(name = "ak_scheduled_program_access_code", columnList = "access_code", unique = true),
                @Index(name = "ix_scheduled_program_program_id", columnList = "program_id"),
                @Index(name = "ix_scheduled_program_patient_id", columnList = "patient_id"),
                @Index(name = "ix_scheduled_program_team_id", columnList = "team_id")
        }
)
@XmlRootElement(name = "scheduled_program")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedEntityGraphs({
        // eagerly fetches created by when used
        @NamedEntityGraph(
                name = "scheduledProgramsWithCreatedBy",
                attributeNodes = {
                        @NamedAttributeNode("createdBy")
                }
        )
})
public class ScheduledProgram extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Pattern(regexp = "\\d{11}", message = "must be 11 numbers in total")
    @Column(name = "access_code", length = 11, nullable = false, columnDefinition = "nvarchar(11)")
    private String accessCode;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "program_id", nullable = false, columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_scheduled_program_rf_emmi"))
    private Program program;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false, columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_scheduled_program_patient"))
    private Patient patient;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", nullable = false, columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_scheduled_program_client_team"))
    private Team team;

    @ManyToOne(optional = true)
    @JoinColumn(name = "provider_id", nullable = true, columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_scheduled_program_provider"))
    private Provider provider;

    @ManyToOne(optional = true)
    @JoinColumn(name = "location_id", nullable = true, columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_scheduled_program_location"))
    private Location location;

    @NotNull
    @Column(name = "view_by_date_utc", nullable = false, columnDefinition = "date")
    private LocalDate viewByDate;

    @Column(name = "active", nullable = false, columnDefinition = "boolean")
    private boolean active = true;

    public ScheduledProgram() {
    }

    /**
     * Id only based constructor
     *
     * @param scheduleId the id
     */
    public ScheduledProgram(Long scheduleId) {
        this.id = scheduleId;
    }

    /**
     * Id and team based constructor
     *
     * @param scheduleId the scheduled id
     * @param team       the team
     */
    public ScheduledProgram(Long scheduleId, Team team) {
        this.id = scheduleId;
        this.team = team;
    }

    @Override
    @XmlElement(name = "created_date")
    public DateTime getCreatedDate() {
        return super.getCreatedDate();
    }

    @Override
    @XmlElement(name = "createdBy")
    public User getCreatedBy() {
        return super.getCreatedBy();
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

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getViewByDate() {
        return viewByDate;
    }

    public void setViewByDate(LocalDate viewByDate) {
        this.viewByDate = viewByDate;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledProgram that = (ScheduledProgram) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ScheduledProgram{" +
                "id=" + id +
                ", version=" + version +
                ", accessCode='" + accessCode + '\'' +
                ", program=" + program +
                ", patient=" + patient +
                ", team=" + team +
                ", provider=" + provider +
                ", location=" + location +
                ", viewByDate=" + viewByDate +
                ", active=" + active +
                '}';
    }
}
