package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.Gender;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Reference data for patient
 */
@XmlRootElement(name = "patient-reference-data")
public class PatientReferenceData extends ResourceSupport {

    @XmlElement(name = "genders")
    @XmlElementWrapper(name = "genders")
    private Gender[] genders = Gender.values();

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
