package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by achavan on 7/14/15.
 */
@Entity
@Audited
@Table(name = "info_header_config")
public class InfoHeaderConfig extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_self_reg_config_id", nullable = false)
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
        InfoHeaderConfig other = (InfoHeaderConfig) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "InfoHeaderConfig{" +
                "id=" + id +
                ", version=" + version +
                ", patientSelfRegConfig=" + patientSelfRegConfig +
                ", language=" + language +
                ", value='" + value + '\'' +
                '}';
    }
}
