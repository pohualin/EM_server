package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a PatientIdLabelConfig entity.
 */
@XmlRootElement(name = "patientIdLabelConfig")
public class PatientIdLabelConfigResource extends ResourceSupport {

    private PatientIdLabelConfig entity;

    public PatientIdLabelConfig getEntity() {
        return entity;
    }

    public void setEntity(PatientIdLabelConfig entity) {
        this.entity = entity;
    }

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
