package com.emmisolutions.emmimanager.web.rest.admin.model.user.client.reference;

import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientRolesAdminResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of UserClientReferenceRoleResource objects.
 */
@XmlRootElement(name = "ref-role-page")
public class UserClientReferenceRolePage extends PagedResource<UserClientReferenceRoleResource> {

    public UserClientReferenceRolePage() {
    }

    /**
     * Wrapper for UserClientReferenceRoleResource objects
     *
     * @param pagedResources to be wrapped
     * @param rolePage       true page
     */
    public UserClientReferenceRolePage(PagedResources<UserClientReferenceRoleResource> pagedResources, Page<UserClientReferenceRole> rolePage) {
        pageDefaults(pagedResources, rolePage);
    }

    /**
     * Link for ref data
     *
     * @return Link reference data for template roles
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.ClientRolesAdminResource#referenceRoles(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, org.springframework.data.web.PagedResourcesAssembler)
     */
    public static Link createReferenceRolesLink() {
        Link link = linkTo(methodOn(ClientRolesAdminResource.class).referenceRoles(null, null, null)).withRel("roles");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
            ));
        return new Link(uriTemplate, link.getRel());
    }

}
