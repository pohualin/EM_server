package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * HATEOAS wrapper for User, essentially a DTO instead of a wrapper.
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
    private List<UserAdminPermissionName> permissions;

    public UserResource() {
    }

    /**
     * Big constructor
     *
     * @param id          id
     * @param version     version
     * @param login       login
     * @param firstName   first name
     * @param lastName    last name
     * @param email       email
     * @param permissions permissions
     */
    public UserResource(Long id, Integer version, String login, String firstName, String lastName, String email, List<UserAdminPermissionName> permissions) {
        this.id = id;
        this.version = version;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.permissions = permissions;
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
