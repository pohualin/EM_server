package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.web.rest.jax_rs.ApiEndpoint;
import com.emmisolutions.emmimanager.web.rest.jax_rs.UsersEndpoint;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.*;

/**
 * The public API for this server
 */
@XmlRootElement(name = "public")
@XmlAccessorType(XmlAccessType.FIELD)
public class PublicApi {

    @XmlElement(name = "self-link")
    private Link self;

    @XmlElement(name = "authenticate-link")
    private Link authenticate;

    @XmlElement(name = "authenticated-link")
    private Link authenticated;

    @XmlElement(name = "logout-link")
    private Link logout;

    @XmlTransient
    private String basePath;

    public PublicApi() {
    }

    public PublicApi(String basePath) {
        this.basePath = basePath;
        self = self();
        authenticated = user();
        authenticate = authenticate();
        logout = logout();
    }

    private Link self() {
        UriBuilder uriBuilder = UriBuilder.fromPath(basePath)
                .path(ApiEndpoint.class, "get");
        return new Link(javax.ws.rs.core.Link.fromUriBuilder(uriBuilder).rel("self").build());
    }

    private Link user() {
        UriBuilder uriBuilder = UriBuilder.fromPath(basePath)
                .path(UsersEndpoint.class, "authenticated");
        return new Link(javax.ws.rs.core.Link.fromUriBuilder(uriBuilder).rel("authenticated").build());
    }

    private Link authenticate() {
        return new Link("authenticate", null, basePath + "authenticate", "");
    }

    private Link logout() {
        return new Link("logout", null, basePath + "logout", "");
    }

}
