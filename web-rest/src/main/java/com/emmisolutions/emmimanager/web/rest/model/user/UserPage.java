package com.emmisolutions.emmimanager.web.rest.model.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.ClientsResource;
import com.emmisolutions.emmimanager.web.rest.resource.UsersResource;

/**
 * A HATEOAS wrapper for a page of UserResource objects.
 */
@XmlRootElement(name = "user-page")
public class UserPage extends PagedResource<UserResource> {

    /**
     * Wrapped constructor
     *
     * @param userResources to be wrapped
     * @param userPage      the raw response
     */
    public UserPage(PagedResources<UserResource> userResources, Page<UserAdmin> userPage) {
        pageDefaults(userResources, userPage);
    }

    /**
     * Creates link used to find to potential owners.
     *
     * @return a <link rel="potentialOwners" href="http://thelink"/>
     * @see com.emmisolutions.emmimanager.web.rest.resource.ClientsResource#getOwnersReferenceData(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler)
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

    /**
     * Creates link used to find users
     *
     */
    public static Link createFullSearchLink() {
        Link link = linkTo(methodOn(UsersResource.class).list(null, null, null, null)).withRel("users");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("term", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("status", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }
}
