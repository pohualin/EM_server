package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.PermissionName;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private Long id;

    private Integer version;

    private String login;

    private String firstName;

    private String lastName;

    private String email;

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private List<PermissionName> permissions;

    public UserResource(){}

    public UserResource(Long id, Integer version, String login, String firstName, String lastName, String email, List<PermissionName> permissions) {
        this.id = id;
        this.version = version;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.permissions = permissions;
    }

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks(){
       return super.getLinks();
    }



}
