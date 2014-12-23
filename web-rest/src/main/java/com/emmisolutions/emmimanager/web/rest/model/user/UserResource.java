package com.emmisolutions.emmimanager.web.rest.model.user;

import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    
    private boolean active;

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private List<UserAdminPermissionName> permissions;
      
    private Set<UserAdminRole> roles;

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
     * @param roles		  roles
     */
    public UserResource(Long id, Integer version, String login, String firstName, String lastName, String email, boolean active, List<UserAdminPermissionName> permissions, Set<UserAdminRole> roles) {
        this.id = id;
        this.version = version;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.permissions = permissions;
        this.roles = roles;
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
