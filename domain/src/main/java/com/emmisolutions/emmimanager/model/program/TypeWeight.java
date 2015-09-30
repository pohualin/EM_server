package com.emmisolutions.emmimanager.model.program;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * The weighting of each emmi type
 */
@Entity
@Table(name = "rf_emmi_type_weight", schema = "program", indexes = {
        @Index(name = "ix_rf_emmi_type_weight", columnList = "type_id")
})
@Audited(targetAuditMode = NOT_AUDITED)
public class TypeWeight {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "type_id", foreignKey =
    @ForeignKey(name = "fk_rf_emmi_type_weight_rf_emmi_type"))
    @JsonBackReference
    private Type type;

    private int weight;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeWeight that = (TypeWeight) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TypeWeight{" +
                "weight=" + weight +
                '}';
    }

}
