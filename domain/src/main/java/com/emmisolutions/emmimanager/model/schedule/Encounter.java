package com.emmisolutions.emmimanager.model.schedule;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;

/**
 * An encounter will be associated with one or more scheduled programs
 * 
 * @see ScheduledProgram
 */
@Entity
@Audited
@Table(name = "encounter")
@XmlRootElement(name = "encounter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Encounter extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Column(name = "encounter_timestamp_utc", nullable = false)
    private LocalDateTime encounterDateTime;

    public Encounter() {
    }

    /**
     * Id only based constructor
     *
     * @param scheduleId
     *            the id
     */
    public Encounter(Long scheduleId) {
        this.id = scheduleId;
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

    public LocalDateTime getEncounterDateTime() {
        return encounterDateTime;
    }

    public void setEncounterDateTime(LocalDateTime encounterDateTime) {
        this.encounterDateTime = encounterDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Encounter that = (Encounter) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Encounter{" + "id=" + id + ", version=" + version
                + ", encounter_date_time=" + encounterDateTime + '}';
    }
}
