package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;

/**
 * The Client User Role Service
 */
public interface UserClientUserClientRoleService {

    /**
     * Creates a new user client user with the passed UserClient object.
     *
     * @param userClientUserClientRole
     *            to create
     * @return the created userClientUserClientRole
     */
    UserClientUserClientRole create(
	    UserClientUserClientRole userClientUserClientRole);

    Page<UserClientUserClientRole> findByUserClient(Long userClientId,
	    Pageable pageable);
}
