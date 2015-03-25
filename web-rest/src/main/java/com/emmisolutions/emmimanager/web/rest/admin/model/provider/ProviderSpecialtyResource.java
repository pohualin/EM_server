package com.emmisolutions.emmimanager.web.rest.admin.model.provider;

import com.emmisolutions.emmimanager.model.ProviderSpecialty;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * HATEOAS wrapper for ProviderSpecialty entities
 */
@XmlRootElement(name="specialty")
public class ProviderSpecialtyResource extends ResourceSupport {

    private ProviderSpecialty entity;

    public ProviderSpecialty getEntity() {
        return entity;
    }

    public void setEntity(ProviderSpecialty entity) {
        this.entity = entity;
    }

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty ("link")
    public List<Link> getLinks(){
        return super.getLinks();
    }

    public ProviderSpecialtyResource(ProviderSpecialty entity) {
        this.entity = entity;
    }
}
