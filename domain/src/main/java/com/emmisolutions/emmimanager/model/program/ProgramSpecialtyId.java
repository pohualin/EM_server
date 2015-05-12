package com.emmisolutions.emmimanager.model.program;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Primary Key/Id for ProgramSpecialty
 */
@Embeddable
public class ProgramSpecialtyId implements Serializable {

    @Column(name="program_cd", nullable = false)
    int programCode;

    @Column(name="specialty_cd", nullable = false)
    int specialtyCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramSpecialtyId that = (ProgramSpecialtyId) o;
        return Objects.equals(programCode, that.programCode) &&
                Objects.equals(specialtyCode, that.specialtyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programCode, specialtyCode);
    }
}
