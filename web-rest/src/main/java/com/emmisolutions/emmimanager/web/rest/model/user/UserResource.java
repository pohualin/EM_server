package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.PermissionName;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * HATEOAS wrapper for User
 */
@XmlRootElement(name = "user")
public class UserResource extends ResourceSupport {

    public Long id;

    public Integer version;

    public String login;

    public String firstName;

    public String lastName;

    public String email;

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    public List<PermissionName> permissions;

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks(){
       return super.getLinks();
    }

}
