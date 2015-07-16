package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a InfoHeaderConfig entity.
 */
@XmlRootElement(name = "infoHeaderConfig")
public class InfoHeaderConfigResource extends ResourceSupport {

    private InfoHeaderConfig entity;

    public InfoHeaderConfig getEntity() {
        return entity;
    }

    public void setEntity(InfoHeaderConfig entity) {
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
