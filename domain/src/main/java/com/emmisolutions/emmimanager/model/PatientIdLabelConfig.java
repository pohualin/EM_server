package com.emmisolutions.emmimanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * a PatientIdLabelConfig
 */
@Entity
@Audited
@Table(name = "patient_id_label_config")
public class PatientIdLabelConfig extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

    @NotNull
//    @Column(name = "id_label_type")
    @JoinColumn(name="id_label_type", nullable = false)
    @ManyToOne
    private PatientIdLabelType idLabelType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_self_reg_config_id", nullable = false)
    @JsonBackReference
    @XmlElement(name = "patientSelfRegConfig")
    @XmlElementWrapper(name = "patientSelfRegConfig")
    @JsonProperty("patientSelfRegConfig")
    private PatientSelfRegConfig patientSelfRegConfig;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id", updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_language_language_id"))
    private Language language;

    @Size(max = 255)
    private String value;


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

    public PatientIdLabelType getIdLabelType() {
        return idLabelType;
    }

    public void setIdLabelType(PatientIdLabelType idLabelType) {
        this.idLabelType = idLabelType;
    }

    public PatientSelfRegConfig getPatientSelfRegConfig() {
        return patientSelfRegConfig;
    }

    public void setPatientSelfRegConfig(PatientSelfRegConfig patientSelfRegConfig) {
        this.patientSelfRegConfig = patientSelfRegConfig;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        PatientIdLabelConfig other = (PatientIdLabelConfig) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PatientIdLabelConfig{" +
                "id=" + id +
                ", version=" + version +
                ", idLabelType=" + idLabelType +
                ", language=" + language +
                ", value='" + value + '\'' +
                ", patientSelfRegConfig=" + patientSelfRegConfig +
                '}';
    }
}
