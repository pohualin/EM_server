package com.emmisolutions.emmimanager.web.rest.admin.model.user.client;

import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of UserClientUserClientRole objects.
 */
@XmlRootElement(name = "user-client-user-client-role-page")
public class UserClientUserClientRolePage extends
        PagedResource<UserClientUserClientRoleResource> {

    public UserClientUserClientRolePage() {
    }

    /**
     * Wrapped constructor
     *
     * @param userClientUserClientRoleResources
     *            to be wrapped
     * @param userClientUserClientRolePage
     *            the raw response
     */
    public UserClientUserClientRolePage(
            PagedResources<UserClientUserClientRoleResource> userClientUserClientRoleResources,
            Page<UserClientUserClientRole> userClientUserClientRolePage) {
        pageDefaults(userClientUserClientRoleResources,
                userClientUserClientRolePage);
    }
}
