package com.emmisolutions.emmimanager.web.rest.client.model.api;

import com.emmisolutions.emmimanager.web.rest.admin.resource.InternationalizationResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.ApiResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsPasswordResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsResource;
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
@XmlRootElement(name = "client-public")
public class PublicApi extends ResourceSupport {

    /**
     * create all the common links to the app
     */
    public PublicApi() {
        Link self = linkTo(ApiResource.class).withSelfRel();
        add(self);
        add(linkTo(methodOn(UserClientsResource.class).authenticated()).withRel("authenticated"));
        add(new Link(self.getHref() + "/authenticate", "authenticate"));
        add(new Link(self.getHref() + "/logout", "logout"));
        add(linkTo(methodOn(UserClientsPasswordResource.class).changeExpiredPassword(null)).withRel("expiredPassword"));
        add(linkTo(methodOn(InternationalizationResource.class).createStringsForLanguage(null)).withRel("messages"));
    }

    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        return super.getLinks();
    }

}
