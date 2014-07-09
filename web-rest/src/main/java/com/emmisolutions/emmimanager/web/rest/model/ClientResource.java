package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.jax_rs.ClientsEndpoint;
import com.emmisolutions.emmimanager.web.rest.jax_rs.TeamsEndpoint;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HATEOAS Client wrapper.
 */
@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientResource {

    private Client entity;

    @XmlElement(name = "link")
    private List<Link> links;

    @XmlTransient
    private String basePath;

    // Required for serialization outbound
    public ClientResource() {
    }

    public ClientResource(Client client, String basePath) {
        this.entity = client;
        this.basePath = basePath;
        this.links = new ArrayList<>();
        this.links.add(linkToSelf());
        this.links.add(linkToTeams());
    }

    private Link linkToSelf() {
        return new Link(javax.ws.rs.core.Link.fromUriBuilder(UriBuilder.fromPath(basePath).path(ClientsEndpoint.class, "get"))
                .rel("self")
                .build(entity.getId()));
    }

    private Link linkToTeams() {
        return new Link(javax.ws.rs.core.Link.fromUriBuilder(UriBuilder.fromPath(basePath).path(TeamsEndpoint.class, "listByClient"))
                .rel("teams")
                .build(entity.getId()));
    }

}
