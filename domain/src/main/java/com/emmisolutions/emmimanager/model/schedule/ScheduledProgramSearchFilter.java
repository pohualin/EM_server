package com.emmisolutions.emmimanager.model.schedule;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.Team;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filter used for Patient searching.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduledProgramSearchFilter {

    @XmlElement(name = "patient")
    @XmlElementWrapper(name = "patients")
    private Set<Patient> patients;

    @XmlElement(name = "accessCode")
    @XmlElementWrapper(name = "accessCodes")
    private Set<String> accessCodes;

    private Long id;

    private Team team;

    private ScheduledProgramSearchFilter() {
    }

    /**
     * Creates a new ScheduledProgramSearchFilter
     *
     * @return a new filter
     */
    public static ScheduledProgramSearchFilter with() {
        return new ScheduledProgramSearchFilter();
    }


    /**
     * Retrieves all patient filters
     *
     * @return the Set of names or null
     */
    public Set<Patient> patients() {
        return patients;
    }

    /**
     * Sets the Patient list
     *
     * @param patients the list of patients
     * @return this ScheduledProgramSearchFilter for chaining
     */
    public ScheduledProgramSearchFilter patients(Patient... patients) {
        if (patients != null) {
            if (this.patients == null) {
                this.patients = new HashSet<>();
            }
            Collections.addAll(this.patients, patients);
        }
        return this;
    }

    /**
     * Sets the patients list
     *
     * @param patients list of patients
     * @return this ScheduledProgramSearchFilter for chaining
     */
    public ScheduledProgramSearchFilter patients(List<Patient> patients) {
        if (!CollectionUtils.isEmpty(patients)) {
            this.patients(patients.toArray(new Patient[patients.size()]));
        }
        return this;
    }

    /**
     * Get access codes which have been set
     *
     * @return Set of access codes
     */
    public Set<String> accessCodes() {
        return accessCodes;
    }

    /**
     * Adds valid access code strings to the filter. Access
     * codes are in the form of 1XXXXXXXXXXX and 2XXXXXXXXXXX
     * where X is a number.
     *
     * @param accessCodes to add
     * @return this ScheduledProgramSearchFilter for chaining
     */
    public ScheduledProgramSearchFilter accessCodes(String... accessCodes) {
        if (accessCodes != null) {
            for (String accessCode : accessCodes) {
                String trimmed = StringUtils.trimToNull(accessCode);
                if (trimmed != null) {
                    if (this.accessCodes == null) {
                        this.accessCodes = new HashSet<>();
                    }
                    this.accessCodes.add(trimmed);
                }
            }
        }
        return this;
    }

    /**
     * List based input for accessCodes
     *
     * @param accessCodes to add
     * @return this ScheduledProgramSearchFilter for chaining
     */
    public ScheduledProgramSearchFilter accessCodes(List<String> accessCodes) {
        if (!CollectionUtils.isEmpty(accessCodes)) {
            this.accessCodes(accessCodes.toArray(new String[accessCodes.size()]));
        }
        return this;
    }

    /**
     * Filter by id
     *
     * @param id to use
     * @return this ScheduledProgramSearchFilter for chaining
     */
    public ScheduledProgramSearchFilter id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Fetches the id filter
     *
     * @return id set on the filter
     */
    public Long id() {
        return id;
    }

    /**
     * Filter by team
     *
     * @param team to use
     * @return this ScheduledProgramSearchFilter for chaining
     */
    public ScheduledProgramSearchFilter team(Team team) {
        this.team = team;
        return this;
    }

    /**
     * Fetches the team filter
     *
     * @return team set on the filter
     */
    public Team team() {
        return team;
    }

    @Override
    public String toString() {
        return "ScheduledProgramSearchFilter{" +
                "id=" + id +
                ", patients=" + patients +
                ", accessCodes=" + accessCodes +
                '}';
    }

}
