package com.emmisolutions.emmimanager.web.rest.model.user.client.reference;

import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A HATEOAS wrapper for a UserClientReferenceRole entity.
 */
@XmlRootElement(name = "reference-role")
public class UserClientReferenceRoleResource extends ResourceSupport {

    private UserClientReferenceRole entity;

    public UserClientReferenceRoleResource() {
    }

    /**
     * Constructor
     *
     * @param entity to wrap
     */
    public UserClientReferenceRoleResource(UserClientReferenceRole entity) {
        this.entity = entity;
    }

    public UserClientReferenceRole getEntity() {
        return entity;
    }

    public void setEntity(UserClientReferenceRole entity) {
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
