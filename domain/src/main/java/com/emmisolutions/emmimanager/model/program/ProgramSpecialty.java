package com.emmisolutions.emmimanager.model.program;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * The relationship between a Program and a Specialty
 */
@Entity
@Table(name = "rf_emmi_specialty_new", schema = "program")
@Audited(targetAuditMode = NOT_AUDITED)
public class ProgramSpecialty {

    private boolean active;

    @EmbeddedId
    private ProgramSpecialtyId programSpecialtyId;

    @ManyToOne
    @MapsId("programCode")
    @JoinColumn(name = "emmi_cd",
            foreignKey = @ForeignKey(name = "fk_rf_emmi_specialty_new_rf_emmi"))
    private Program program;

    @ManyToOne
    @MapsId("specialtyCode")
    @JoinColumn(name = "specialty_cd",
            foreignKey = @ForeignKey(name = "fk_rf_emmi_specialty_new_rf_specialty_new"))
    private Specialty specialty;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public ProgramSpecialtyId getProgramSpecialtyId() {
        return programSpecialtyId;
    }

    public void setProgramSpecialtyId(ProgramSpecialtyId programSpecialtyId) {
        this.programSpecialtyId = programSpecialtyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramSpecialty that = (ProgramSpecialty) o;
        return Objects.equals(programSpecialtyId, that.programSpecialtyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programSpecialtyId);
    }
}
