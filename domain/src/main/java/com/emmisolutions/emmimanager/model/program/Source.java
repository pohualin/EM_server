package com.emmisolutions.emmimanager.model.program;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * Program Source
 */
@Entity
@Table(name = "rf_emmi_source", schema = "program")
@Audited(targetAuditMode = NOT_AUDITED)
public class Source {

    @Id
    @Column(name = "emmi_src_cd")
    private Integer id;

    private boolean active;

    @Column(name = "emmi_src_nm")
    private String name;

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
        Source source = (Source) o;
        return Objects.equals(id, source.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
