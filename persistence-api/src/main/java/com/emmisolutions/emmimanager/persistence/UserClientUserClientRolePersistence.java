package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;

/**
 * Persistence API for UserClient roles and permissions.
 */
public interface UserClientUserClientRolePersistence {

    /**
     * Saves or updates the UserClientUserClientRole object
     *
     * @param userClientUserClientRole
     *            the UserClientUserClientRole to save or create
     * @return the saved UserClientUserClientRole
     */
    UserClientUserClientRole saveOrUpdate(
	    UserClientUserClientRole userClientUserClientRole);

    Page<UserClientUserClientRole> findByUserClient(UserClient userClient,
	    Pageable pageable);
}
