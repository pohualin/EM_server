package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.jax_rs.ClientsEndpoint;
import com.emmisolutions.emmimanager.web.rest.jax_rs.TeamsEndpoint;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * HATEOAS Team Wrapper.
 */
@XmlRootElement(name = "entity")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamResource {

    private Team entity;

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    @XmlTransient
    private String basePath;

    public TeamResource() {
    }

    public TeamResource(Team team, String basePath) {
        this.entity = team;
        this.basePath = basePath;
        this.links = new ArrayList<>();
        this.links.add(linkToSelf());
        this.links.add(linkToClient());
    }

    private Link linkToSelf() {
        return Link.fromUriBuilder(UriBuilder.fromPath(basePath).path(TeamsEndpoint.class, "get"))
                .rel("self")
                .build(entity.getId());
    }

    private Link linkToClient() {
        return Link.fromUriBuilder(UriBuilder.fromPath(basePath).path(ClientsEndpoint.class, "get"))
                .rel("client")
                .build(entity.getClient().getId());
    }

}
