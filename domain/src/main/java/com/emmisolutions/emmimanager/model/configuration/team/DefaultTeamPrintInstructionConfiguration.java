package com.emmisolutions.emmimanager.model.configuration.team;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * This model holds the global default team print instruction configuration for
 * all teams. Configurations contain default, minimum and maximum values.
 * 
 * @see TeamPrintInstructionConfiguration
 * 
 */
@Audited
@Entity
@Table(name = "default_team_print_instruction_configuration")
@XmlRootElement(name = "default_team_print_instruction_configuration")
public class DefaultTeamPrintInstructionConfiguration extends
        AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @NotNull
    @Column(name="patient_url", columnDefinition = "nvarchar(255)", nullable = false)
    private String patientUrl;

    /**
     * Default constructor
     */
    public DefaultTeamPrintInstructionConfiguration() {

    }

    /**
     * ID constructor
     *
     * @param id
     *            to use
     */
    public DefaultTeamPrintInstructionConfiguration(Long id) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPatientUrl() {
        return patientUrl;
    }

    public void setPatientUrl(String patientUrl) {
        this.patientUrl = patientUrl;
    }

    @Override
    public String toString() {
        return "DefaultPasswordConfiguration{" + "active=" + active + ", id="
                + id + ", version=" + version + ", patientUrl='" + patientUrl
                + '\'' + '}';
    }
}
