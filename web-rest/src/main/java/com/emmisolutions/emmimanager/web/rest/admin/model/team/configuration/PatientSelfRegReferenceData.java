package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.PatientIdLabelType;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Reference data for patient self reg config
 */
@XmlRootElement(name = "patient-self-reg-reference-data")
public class PatientSelfRegReferenceData extends BaseResource<PatientIdLabelType> {

    @XmlElement(name = "idLabelTypes")
    @XmlElementWrapper(name = "idLabelTypes")
    private PatientIdLabelType[] idLabelTypes = PatientIdLabelType.values();

    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        return super.getLinks();
    }

}
