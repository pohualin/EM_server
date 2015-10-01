package com.emmisolutions.emmimanager.model.program;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * A Program Type
 */
@Entity
@Table(name = "rf_emmi_type", schema = "program")
@Audited(targetAuditMode = NOT_AUDITED)
public class Type {

    @Id
    @Column(name = "emmi_tp_cd")
    private Integer id;

    @Column(name = "emmi_tp_nm")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "type")
    private TypeWeight typeWeight;

    private boolean active;

    public Type() {
    }

    /**
     * Type by id
     *
     * @param id the id
     */
    public Type(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TypeWeight getTypeWeight() {
        return typeWeight;
    }

    public void setTypeWeight(TypeWeight typeWeight) {
        this.typeWeight = typeWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return Objects.equals(id, type.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                '}';
    }
}
