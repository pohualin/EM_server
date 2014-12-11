package com.emmisolutions.emmimanager.web.rest.model.user.client.team;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A HATEOAS wrapper for a UserClientUserClientTeamRole object
 */
public class UserClientUserClientTeamRoleResource extends ResourceSupport {

    private UserClientUserClientTeamRole entity;

    public UserClientUserClientTeamRoleResource() {
    }

    /**
     * Constructor that takes the wrapped entity as an argument
     *
     * @param entity
     *            to be wrapped
     */
    public UserClientUserClientTeamRoleResource(
	    UserClientUserClientTeamRole entity) {
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

    public UserClientUserClientTeamRole getEntity() {
	return entity;
    }

    public void setEntity(UserClientUserClientTeamRole entity) {
	this.entity = entity;
    }
}
