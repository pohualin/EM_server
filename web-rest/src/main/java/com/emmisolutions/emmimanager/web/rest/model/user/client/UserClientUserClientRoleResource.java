package com.emmisolutions.emmimanager.web.rest.model.user.client;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HATEOAS wrapper for UserClientUserClientRole, essentially a DTO instead of a
 * wrapper.
 */
@XmlRootElement(name = "user-client-user-client-role")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientUserClientRoleResource extends ResourceSupport {

    private UserClientUserClientRole entity;

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

    public UserClientUserClientRole getEntity() {
        return entity;
    }

    public void setEntity(UserClientUserClientRole entity) {
        this.entity = entity;
    }

}
