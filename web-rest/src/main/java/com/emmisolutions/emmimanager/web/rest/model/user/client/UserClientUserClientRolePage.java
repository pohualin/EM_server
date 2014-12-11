package com.emmisolutions.emmimanager.web.rest.model.user.client;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.web.rest.model.PagedResource;

/**
 * A HATEOAS wrapper for a page of UserResource objects.
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
