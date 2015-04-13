package com.emmisolutions.emmimanager.model.program;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    private boolean active;

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
}
