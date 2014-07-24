package com.emmisolutions.emmimanager.web.rest.model.api;

import com.emmisolutions.emmimanager.web.rest.resource.ApiResource;
import com.emmisolutions.emmimanager.web.rest.resource.UsersResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * The public API for this server
 */
@XmlRootElement(name = "public")
@XmlAccessorType(XmlAccessType.FIELD)
public class PublicApi extends ResourceSupport {

    @XmlElement(name = "link", namespace = Link.ATOM_NAMESPACE)
    @XmlElementWrapper(name = "links")
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
