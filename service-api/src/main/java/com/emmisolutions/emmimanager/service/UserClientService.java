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
     * Reload an existing client user with the passed userClientId.
     *
     * @param userClient
     *            to use
     * @return the existing UserClient
     */
    UserClient reload(UserClient userClient);

    /**
     * Update an existing UserClient with the passed in UserClient object.
     *
     * @param user
     *            to update
     * @return the created user
     */
    UserClient update(UserClient user);

    /**
     * Find existing UserClient with the passed in UserClientSearchFilter.
     *
     * @param filter
     *            to search
     * @return pageable UserClient
     */
    Page<UserClient> list(Pageable pageable, UserClientSearchFilter filter);

}
