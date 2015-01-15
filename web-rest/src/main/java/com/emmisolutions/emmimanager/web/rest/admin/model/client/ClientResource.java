package com.emmisolutions.emmimanager.web.rest.admin.model.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientsResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a Client entity.
 */
@XmlRootElement(name = "client")
public class ClientResource extends ResourceSupport {

    private Client entity;

    public Client getEntity() {
        return entity;
    }

    public void setEntity(Client entity) {
        this.entity = entity;
    }

    /**
     * The link to find the normalized client by name
     * @return the link
     */
    public static Link createFindNormalizedNameLink() {
        Link link = linkTo(methodOn(ClientsResource.class).findByNormalizedName(null)).withRel("findByNormalizedName");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("normalizedName", TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(uriTemplate, link.getRel());
    }
    
    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks(){
        return super.getLinks();
    }
}
