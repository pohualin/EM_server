package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * A patient self registration configuration
 */
@Entity
@Audited
@Table(name = "patient_self_reg_config")
public class PatientSelfRegConfig extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "expose_name", columnDefinition = "boolean", nullable = false)
    private boolean exposeName;

    @Column(name = "require_name", columnDefinition = "boolean", nullable = false)
    private boolean requireName;

    @Column(name = "expose_id", columnDefinition = "boolean", nullable = false)
    private boolean exposeId;

    @Column(name = "require_id", columnDefinition = "boolean", nullable = false)
    private boolean requireId;

    @Column(name = "expose_dob", columnDefinition = "boolean", nullable = false)
    private boolean exposeDateOfBirth;

    @Column(name = "require_dob", columnDefinition = "boolean", nullable = false)
    private boolean requireDateOfBirth;

    @Column(name = "expose_email", columnDefinition = "boolean", nullable = false)
    private boolean exposeEmail;

    @Column(name = "require_email", columnDefinition = "boolean", nullable = false)
    private boolean requireEmail;

    @Column(name = "expose_phone", columnDefinition = "boolean", nullable = false)
    private boolean exposePhone;

    @Column(name = "require_phone", columnDefinition = "boolean", nullable = false)
    private boolean requirePhone;

//    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id_label_type_id"/*, nullable = false*/)
    private PatientIdLabelType patientIdLabelType;


    public PatientIdLabelType getPatientIdLabelType() {
        return patientIdLabelType;
    }

    public void setPatientIdLabelType(PatientIdLabelType patientIdLabelType) {
        this.patientIdLabelType = patientIdLabelType;
    }

    public PatientSelfRegConfig() {
    }

    public PatientSelfRegConfig(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isExposeName() {
        return exposeName;
    }

    public void setExposeName(boolean exposeName) {
        this.exposeName = exposeName;
    }

    public boolean isRequireName() {
        return requireName;
    }

    public void setRequireName(boolean requireName) {
        this.requireName = requireName;
    }

    public boolean isExposeId() {
        return exposeId;
    }

    public void setExposeId(boolean exposeId) {
        this.exposeId = exposeId;
    }

    public boolean isRequireId() {
        return requireId;
    }

    public void setRequireId(boolean requireId) {
        this.requireId = requireId;
    }

    public boolean isExposeDateOfBirth() {
        return exposeDateOfBirth;
    }

    public void setExposeDateOfBirth(boolean exposeDateOfBirth) {
        this.exposeDateOfBirth = exposeDateOfBirth;
    }

    public boolean isRequireDateOfBirth() {
        return requireDateOfBirth;
    }

    public void setRequireDateOfBirth(boolean requireDateOfBirth) {
        this.requireDateOfBirth = requireDateOfBirth;
    }

    public boolean isExposeEmail() {
        return exposeEmail;
    }

    public void setExposeEmail(boolean exposeEmail) {
        this.exposeEmail = exposeEmail;
    }

    public boolean isRequireEmail() {
        return requireEmail;
    }

    public void setRequireEmail(boolean requireEmail) {
        this.requireEmail = requireEmail;
    }

    public boolean isExposePhone() {
        return exposePhone;
    }

    public void setExposePhone(boolean exposePhone) {
        this.exposePhone = exposePhone;
    }

    public boolean isRequirePhone() {
        return requirePhone;
    }

    public void setRequirePhone(boolean requirePhone) {
        this.requirePhone = requirePhone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PatientSelfRegConfig other = (PatientSelfRegConfig) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PatientSelfRegConfig{" +
                "id=" + id +
                ", version=" + version +
                ", team=" + team +
                ", exposeName=" + exposeName +
                ", requireName=" + requireName +
                ", exposeId=" + exposeId +
                ", requireId=" + requireId +
                ", exposeDateOfBirth=" + exposeDateOfBirth +
                ", requireDateOfBirth=" + requireDateOfBirth +
                ", exposeEmail=" + exposeEmail +
                ", requireEmail=" + requireEmail +
                ", exposePhone=" + exposePhone +
                ", requirePhone=" + requirePhone +
                '}';
    }
}
