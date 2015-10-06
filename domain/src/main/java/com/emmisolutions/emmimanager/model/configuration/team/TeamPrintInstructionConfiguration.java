package com.emmisolutions.emmimanager.model.configuration.team;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Team;

/**
 * TeamPrintInstructionConfiguration Domain Object with a reference to
 * DefaultTeamPrintInstructionConfiguration for validation purpose.
 * 
 * @see DefaultTeamPrintInstructionConfiguration
 * 
 */
@Audited
@Entity
@XmlRootElement(name = "team_print_instruction_configuration")
@Table(name = "team_print_instruction_configuration")
public class TeamPrintInstructionConfiguration extends AbstractAuditingEntity
        implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @OneToOne
    @JoinColumn(name = "client_team_id", nullable = false)
    private Team team;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "default_team_print_instruction_configuration_id", nullable = false)
    private DefaultTeamPrintInstructionConfiguration defaultTeamPrintInstructionConfiguration;

    @NotNull
    @Column(name = "patient_url", columnDefinition = "nvarchar(255)", nullable = false)
    private String patientUrl;

    /**
     * Default constructor
     */
    public TeamPrintInstructionConfiguration() {

    }

    /**
     * id constructor
     * 
     * @param id
     *            to use
     */
    public TeamPrintInstructionConfiguration(Long id) {
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

    public DefaultTeamPrintInstructionConfiguration getDefaultTeamPrintInstructionConfiguration() {
        return defaultTeamPrintInstructionConfiguration;
    }

    public void setDefaultTeamPrintInstructionConfiguration(
            DefaultTeamPrintInstructionConfiguration defaultTeamPrintInstructionConfiguration) {
        this.defaultTeamPrintInstructionConfiguration = defaultTeamPrintInstructionConfiguration;
    }

    public String getPatientUrl() {
        return patientUrl;
    }

    public void setPatientUrl(String patientUrl) {
        this.patientUrl = patientUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TeamPrintInstructionConfiguration that = (TeamPrintInstructionConfiguration) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientPasswordConfiguration{" + "team=" + team + ", id=" + id
                + ", version=" + version + ", defaultTeamPrintInstruction="
                + defaultTeamPrintInstructionConfiguration + ", patientUrl='"
                + patientUrl + '}';
    }
}
