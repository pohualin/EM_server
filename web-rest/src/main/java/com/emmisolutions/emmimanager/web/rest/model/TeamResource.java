package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.spring.ClientsResource;
import com.emmisolutions.emmimanager.web.rest.spring.TeamsResource;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS Team Wrapper.
 */
@XmlRootElement(name = "entity")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamResource {

    private Team entity;

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    private List<Link> links;

    @XmlTransient
    private String basePath;

    public TeamResource() {
    }

    public TeamResource(Team team) {
        this.entity = team;
        this.links = new ArrayList<>();
        this.links.add(linkToSelf());
        this.links.add(linkToClient());
    }

    private Link linkToSelf() {
        return new Link(linkTo(methodOn(TeamsResource.class).get(entity.getId())).withSelfRel());
    }

    private Link linkToClient() {
        return new Link(linkTo(methodOn(ClientsResource.class).get(entity.getClient().getId())).withRel("client"));
    }

}
