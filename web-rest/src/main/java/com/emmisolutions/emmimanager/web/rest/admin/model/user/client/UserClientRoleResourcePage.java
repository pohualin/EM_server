package com.emmisolutions.emmimanager.web.rest.admin.model.user.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientRolesAdminResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A Page of UserClientRoleResource objects
 */
public class UserClientRoleResourcePage extends
        PagedResource<UserClientRoleResource> {

    public UserClientRoleResourcePage() {
    }

    /**
     * Wrapped constructor
     *
     * @param userClientRoleResource
     *            to be wrapped
     * @param userClientRolePage
     *            the raw response
     */
    public UserClientRoleResourcePage(
            PagedResources<UserClientRoleResource> userClientRoleResource,
            Page<UserClientRole> userClientRolePage) {
        pageDefaults(userClientRoleResource, userClientRolePage);
    }

    /**
     * Create the search link
     *
     * @param client
     *            to load for
     * @return Link for roles on a client
     * @see com.emmisolutions.emmimanager.web.rest.admin.resource.ClientRolesAdminResource#clientRoles(Long,
     *      org.springframework.data.domain.Pageable,
     *      org.springframework.data.web.PagedResourcesAssembler)
     */
    public static Link createFullSearchLink(Client client) {
        Link link = linkTo(
                methodOn(ClientRolesAdminResource.class).clientRoles(
                        client.getId(), null, null)).withRel("roles");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page",
                                TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable(
                                "size",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable(
                                "sort",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

}
