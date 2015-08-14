package com.emmisolutions.emmimanager.model.program;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * A Brand
 */
@Entity
@Table(name = "rf_emmi_brand", schema = "program")
@Audited(targetAuditMode = NOT_AUDITED)
public class Brand {

    @Id
    @Column(name = "emmi_brnd_cd")
    private Integer id;

    private boolean active;

    @Column(name = "emmi_brnd_dscrptn")
    private String description;

    public Brand() {
    }

    /**
     * Brand by ID
     *
     * @param id to use
     */
    public Brand(Integer id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return Objects.equals(id, brand.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
