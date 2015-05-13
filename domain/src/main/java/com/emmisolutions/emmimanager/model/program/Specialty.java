package com.emmisolutions.emmimanager.model.program;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * A program specialty.
 */
@Entity
@Table(name = "rf_specialty_new", schema = "program")
@Audited(targetAuditMode = NOT_AUDITED)
public class Specialty {

    @Id
    @Column(name = "specialty_cd")
    private Integer id;

    private boolean active;

    @Column(name = "specialty_clnt_nm")
    private String name;

    public Specialty() {
    }

    /**
     * Specialty with the id/code set
     *
     * @param id the id
     */
    public Specialty(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialty specialty = (Specialty) o;
        return Objects.equals(id, specialty.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
