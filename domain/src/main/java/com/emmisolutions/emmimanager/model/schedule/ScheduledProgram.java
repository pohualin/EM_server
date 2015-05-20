package com.emmisolutions.emmimanager.model.schedule;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.constraint.DammChecksum;
import com.emmisolutions.emmimanager.model.program.Program;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class ScheduledProgram extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @DammChecksum(message = "Invalid code (checksum)")
    @Pattern(regexp = "2\\d{10}", message = "must start with a 2 and be 11 numbers in total")
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

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false, columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_scheduled_program_location"))
    private Location location;

    @NotNull
    @Column(name = "view_by_date", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime viewByDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getViewByDate() {
        return viewByDate;
    }

    public void setViewByDate(LocalDateTime viewByDate) {
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
