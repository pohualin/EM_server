package com.emmisolutions.emmimanager.web.rest.model;

import com.emmisolutions.emmimanager.web.rest.spring.ApiResource;
import com.emmisolutions.emmimanager.web.rest.spring.UsersResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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


    public PublicApi (){
        self = self();
        authenticated = user();
        authenticate = authenticate();
        logout = logout();
    }

    private Link self() {
        return new Link(linkTo(ApiResource.class).withSelfRel());
    }

    private Link user() {
        return new Link(linkTo(methodOn(UsersResource.class).authenticated()).withRel("authenticated"));
    }

    private Link authenticate() {
        return new Link("authenticate", null, "webapi/authenticate", "");
    }

    private Link logout() {
        return new Link("logout", null, "webapi/logout", "");
    }

}
