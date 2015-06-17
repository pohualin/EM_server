package com.emmisolutions.emmimanager.web.rest.admin.model.patient;

import com.emmisolutions.emmimanager.model.PatientOptOutPreference;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Reference data for client editing
 */
@XmlRootElement(name = "patient-reference-data")
public class ReferenceData extends BaseResource<PatientOptOutPreference> {

    @XmlElement(name = "optOutPreference")
    @XmlElementWrapper(name = "optOutPreferences")
    private Set<PatientOptOutPreference> optOutPreferences;

    /**
     * Constructor with data
     *
     * @param optOutPreferences opt out preferences
     */
    public ReferenceData(Collection<PatientOptOutPreference> optOutPreferences) {
        this.optOutPreferences = new HashSet<>(optOutPreferences);
    }

}
