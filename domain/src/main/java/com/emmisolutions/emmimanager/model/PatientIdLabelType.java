package com.emmisolutions.emmimanager.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Patient Id Label Types
 * <p/>
 * PATIENT_SELF_REG_LABEL_PATIENT_ID,
 * PATIENT_SELF_REG_LABEL_MEMBER_ID,
 * PATIENT_SELF_REG_LABEL_MEDICAL_RECORD_NUMBER,
 * PATIENT_SELF_REG_LABEL_MRN,
 * PATIENT_SELF_REG_LABEL_OTHER
 */
@Audited
@Entity
@Table(name = "patient_id_label_type", uniqueConstraints = @UniqueConstraint(name = "uk_patient_id_label_type_key_path", columnNames = "key_path"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PatientIdLabelType extends AbstractTypeEntity {

    @Column(name = "is_modifiable", columnDefinition = "boolean", nullable = false)
    private boolean isModifiable;

    public boolean isModifiable() {
        return isModifiable;
    }

    public void setIsModifiable(boolean isModifiable) {
        this.isModifiable = isModifiable;
    }

    public PatientIdLabelType() {
    }

    /**
     * Primary key constructor
     *
     * @param id the id
     */
    public PatientIdLabelType(Long id) {
        setId(id);
    }


}
