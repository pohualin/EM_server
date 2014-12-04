package com.emmisolutions.emmimanager.web.rest.model.provider;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A team provider
 */
@XmlRootElement(name = "team-provider")
public class TeamProviderResource extends ResourceSupport {

    private TeamProvider entity;

    public TeamProvider getEntity() {
        return entity;
    }

    public void setEntity(TeamProvider entity) {
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
