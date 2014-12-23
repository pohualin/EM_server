package com.emmisolutions.emmimanager.web.rest.model.user.client;

import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * A HATEOAS wrapper for a UserClientRole object
 */
public class UserClientRoleResource extends ResourceSupport {

    private UserClientRole entity;

    public UserClientRoleResource() {
    }

    /**
     * Constructor that takes the wrapped entity as an argument
     *
     * @param entity
     *            to be wrapped
     */
    public UserClientRoleResource(UserClientRole entity) {
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

    public UserClientRole getEntity() {
        return entity;
    }

    public void setEntity(UserClientRole entity) {
        this.entity = entity;
    }
}
