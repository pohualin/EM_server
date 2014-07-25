package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.PermissionName;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * HATEOAS wrapper for User
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
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

    @XmlElement(name = "link", namespace = Link.ATOM_NAMESPACE)
    @XmlElementWrapper(name = "links")
    public List<Link> getLinks(){
       return super.getLinks();
    }

}
