package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.web.rest.resource.ClientsResource;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.*;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A User Page. We need the wrapper for JAXB to work properly
 */
@XmlSeeAlso({Resource.class, User.class, UserResource.class})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "client-search-result")
@XmlType(propOrder = {"metadata", "links", "content"})
public class UserPage {

    @XmlElement(name = "page")
    private PagedResources.PageMetadata metadata;

    @XmlElement(name = "link", namespace = Link.ATOM_NAMESPACE)
    @XmlElementWrapper(name = "links")
    private List<Link> links;

    @XmlElement(name = "content")
    @XmlElementWrapper(name = "contents")
    private Collection<UserResource> content;

    public UserPage(PagedResources<UserResource> userResources){
        this.metadata = userResources.getMetadata();
        this.content = userResources.getContent();
        this.links = userResources.getLinks();
    }

    public UserPage(){

    }

    public static Link createPotentialOwnersFullSearchLink(){
        Link link = linkTo(methodOn(ClientsResource.class).getOwnersReferenceData(null, null, null)).withRel("potentialOwners");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }
}
