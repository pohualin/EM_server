package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.ClientsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of UserResource objects.
 */
@XmlRootElement(name = "user-page")
public class UserPage extends PagedResource<UserResource> {

    public UserPage() {
    }

    public UserPage(PagedResources<UserResource> userResources, Page<User> userPage) {
        pageDefaults(userResources, userPage);
    }

    /**
     * Creates link used to find to potential owners.
     *
     * @see com.emmisolutions.emmimanager.web.rest.resource.ClientsResource#getOwnersReferenceData(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler)
     * @return a <link rel="potentialOwners" href="http://thelink"/>
     */
    public static Link createPotentialOwnersFullSearchLink() {
        Link link = linkTo(methodOn(ClientsResource.class).getOwnersReferenceData(null, null, null)).withRel("potentialOwners");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }
}
