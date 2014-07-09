package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.web.rest.jax_rs.ApiEndpoint;
import com.emmisolutions.emmimanager.web.rest.jax_rs.UsersEndpoint;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The public API for this server
 */
@XmlRootElement(name = "api")
@XmlAccessorType(XmlAccessType.FIELD)
public class PublicApi {

    @XmlElement(name = "link")
    private List<Link> links;

    @XmlTransient
    private String basePath;

    public PublicApi() {
    }

    public PublicApi(String basePath) {
        this.basePath = basePath;
        links = new ArrayList<>();
        links.add(selfLink());
        links.add(getUserLink());
    }

    private Link selfLink() {
        UriBuilder uriBuilder = UriBuilder.fromPath(basePath)
                .path(ApiEndpoint.class, "get");
        return new Link(javax.ws.rs.core.Link.fromUriBuilder(uriBuilder).rel("self").build());
    }

    private Link getUserLink() {
        UriBuilder uriBuilder = UriBuilder.fromPath(basePath)
                .path(UsersEndpoint.class, "authenticated");
        return new Link(javax.ws.rs.core.Link.fromUriBuilder(uriBuilder).rel("authenticated").build());
    }

}
