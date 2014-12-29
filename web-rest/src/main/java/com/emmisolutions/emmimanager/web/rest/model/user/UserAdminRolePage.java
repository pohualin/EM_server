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

import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.resource.UsersResource;

/**
 * A HATEOAS wrapper for a page of UserResource objects.
 */
@XmlRootElement(name = "userAdminRole-page")
public class UserAdminRolePage extends PagedResource<UserAdminRoleResource> {

    /**
     * Wrapped constructor
     *
     * @param userResources to be wrapped
     * @param userPage      the raw response
     */
    public UserAdminRolePage(PagedResources<UserAdminRoleResource> userResources, Page<UserAdminRole> userPage) {
        pageDefaults(userResources, userPage);
    }
    
    /**
     * Creates link used to find user admin roles
     *
     */
    public static Link createUserAdminRolesLink() {
        Link link = linkTo(methodOn(UsersResource.class).userAdminRoles(null, null)).withRel("userAdminRoles");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

}
