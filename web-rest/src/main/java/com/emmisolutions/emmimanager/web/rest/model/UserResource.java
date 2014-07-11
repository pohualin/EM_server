package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.web.rest.jax_rs.UsersEndpoint;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HATEOAS User wrapper.
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserResource {

    private User entity;

    @XmlElement(name = "link")
    private List<Link> links;

    @XmlTransient
    private String basePath;

    // Required for serialization outbound
    public UserResource() {
    }

    // TODO: Probably want to wrap a UserDetails for the authenticated call instead of User
    public UserResource(User user, String basePath) {
        this.entity = user;
        this.basePath = basePath;
        this.links = new ArrayList<>();
        this.links.add(linkToSelf());

        // TODO: deal with permissions here maybe
        this.links.add(ClientsResource.searchLink(basePath));

        this.links.add(ClientsResource.createLink(basePath));
    }

    private Link linkToSelf() {
        return new Link(javax.ws.rs.core.Link.fromUriBuilder(
                UriBuilder.fromPath(basePath).path(UsersEndpoint.class, "get"))
                .rel("self")
                .build(entity.getId()));
    }
}
