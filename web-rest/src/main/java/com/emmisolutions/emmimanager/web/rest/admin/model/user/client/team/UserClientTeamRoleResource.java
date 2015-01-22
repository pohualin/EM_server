package com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * A HATEOAS wrapper for a UserClientTeamRole object
 */
public class UserClientTeamRoleResource extends ResourceSupport {

    private UserClientTeamRole entity;

    public UserClientTeamRoleResource() {
    }

    /**
     * Constructor that takes the wrapped entity as an argument
     *
     * @param entity to be wrapped
     */
    public UserClientTeamRoleResource(UserClientTeamRole entity) {
        this.setEntity(entity);
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

    public UserClientTeamRole getEntity() {
        return entity;
    }

    public void setEntity(UserClientTeamRole entity) {
        this.entity = entity;
    }
}
