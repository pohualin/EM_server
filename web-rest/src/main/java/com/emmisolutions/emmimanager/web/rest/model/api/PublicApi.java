package com.emmisolutions.emmimanager.web.rest.model.api;

import com.emmisolutions.emmimanager.web.rest.resource.ApiResource;
import com.emmisolutions.emmimanager.web.rest.resource.UsersResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * The public API for this server
 */
@XmlRootElement(name = "public")
public class PublicApi extends ResourceSupport {

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks(){
        return super.getLinks();
    }

    public PublicApi() {
        Link self = linkTo(ApiResource.class).withSelfRel();
        add(self);
        add(linkTo(methodOn(UsersResource.class).authenticated()).withRel("authenticated"));
        add(new Link(self.getHref() + "/authenticate", "authenticate"));
        add(new Link(self.getHref() + "/logout", "logout"));
    }

}
