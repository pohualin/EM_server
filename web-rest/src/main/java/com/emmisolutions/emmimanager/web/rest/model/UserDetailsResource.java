package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.model.Permission;
import com.emmisolutions.emmimanager.model.Role;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.web.rest.spring.UsersResource;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS User wrapper.
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDetailsResource {

    private String login;

    private String firstName;

    private String lastName;

    private String email;

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private List<String> permissions;

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    private List<Link> links;

    // Required for serialization outbound
    public UserDetailsResource() {
    }

    public UserDetailsResource(User user) {
        List<String> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                roles.add(permission.getName().toString());
            }
        }
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.permissions = roles;
        this.links = new ArrayList<>();
        this.links.add(linkToSelf());
        this.links.add(ClientPageResource.searchLink());
        this.links.add(ClientPageResource.createLink());
    }

    private Link linkToSelf() {
        return new Link(linkTo(methodOn(UsersResource.class).authenticated()).withSelfRel());
    }
}
