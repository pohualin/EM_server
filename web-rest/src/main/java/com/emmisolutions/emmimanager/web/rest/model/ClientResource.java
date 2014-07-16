package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.spring.ClientsResource;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS Client wrapper.
 */
@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientResource {

    private Client entity;

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    private List<Link> links;

    // Required for serialization outbound
    public ClientResource() {
    }

    public ClientResource(Client client) {
        this.entity = client;
        this.links = new ArrayList<>();
        this.links.add(linkToSelf());
//        this.links.add(linkToTeams());
    }

    private Link linkToSelf() {
        return new Link(linkTo(methodOn(ClientsResource.class).get(entity.getId())).withSelfRel());
    }

//    private Link linkToTeams() {
//        return new Link(linkTo(methodOn(TeamsResource.class).listByClient(entity.getId(), null, null)).withRel("teams"));
//    }

}
