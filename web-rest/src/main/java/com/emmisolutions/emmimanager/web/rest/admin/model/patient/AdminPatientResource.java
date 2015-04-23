package com.emmisolutions.emmimanager.web.rest.admin.model.patient;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * A patient resource
 */
@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminPatientResource extends BaseResource<Patient> {

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
