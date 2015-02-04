package com.emmisolutions.emmimanager.web.rest.admin.model.user;

import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import javax.xml.bind.annotation.*;
import java.util.List;

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
