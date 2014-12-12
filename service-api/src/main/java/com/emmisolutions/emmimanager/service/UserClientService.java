package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * The UserClient Service
 */
public interface UserClientService {

    /**
     * Creates a new UserClient with the passed UserClient object.
     *
     * @param user
     *            to create
     * @return the created user
     */
    UserClient create(UserClient user);

    /**
     * Reload an existing UserClient with the passed userClientId.
     *
     * @param userClientId
     *            to lookup
     * @return the existing UserClient
     */
    UserClient reload(UserClient userClient);

    /**
     * Update an existing UserClient with the passed UserClient object.
     *
     * @param user
     *            to update
     * @return the created user
     */
    UserClient update(UserClient user);

    /**
     * Find existing UserClient with the passed UserClientSearchFilter.
     *
     * @param UserClientSearchFilter
     *            to search
     * @return pageable UserClient
     */
    Page<UserClient> list(Pageable pageable, UserClientSearchFilter filter);

}
