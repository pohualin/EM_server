package com.emmisolutions.emmimanager.web.rest.model.user;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;

import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.web.rest.model.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * HATEOAS wrapper for User Admin Role, essentially a DTO instead of a wrapper.
 */
@XmlRootElement(name = "userAdminRole")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserAdminRoleResource extends BaseResource<UserAdminRole> {

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
