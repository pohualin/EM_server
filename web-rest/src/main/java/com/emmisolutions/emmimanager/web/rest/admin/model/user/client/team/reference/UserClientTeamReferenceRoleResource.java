package com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.reference;

import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a UserClientReferenceTeamRole entity.
 */
@XmlRootElement(name = "reference-role")
public class UserClientTeamReferenceRoleResource extends ResourceSupport {

    private UserClientReferenceTeamRole entity;

    public UserClientTeamReferenceRoleResource() {
    }

    /**
     * Constructor
     *
     * @param entity to wrap
     */
    public UserClientTeamReferenceRoleResource(UserClientReferenceTeamRole entity) {
        this.entity = entity;
    }

    public UserClientReferenceTeamRole getEntity() {
        return entity;
    }

    public void setEntity(UserClientReferenceTeamRole entity) {
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
