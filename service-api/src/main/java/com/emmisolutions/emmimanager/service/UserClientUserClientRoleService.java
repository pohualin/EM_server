package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;

/**
 * The UserClientUserClientRole Service
 */
public interface UserClientUserClientRoleService {

    /**
     * Creates a new UserClientUserClientRole with the passed
     * UserClientUserClientRole object.
     *
     * @param userClientUserClientRole
     *            to create
     * @return the created UserClientUserClientRole
     */
    UserClientUserClientRole create(
            UserClientUserClientRole userClientUserClientRole);

    /**
     * @param userClient
     *            to be used
     * @param pageable
     *            to be used
     * @return a page of UserClientUserClientRole having passed in userClientId
     */
    Page<UserClientUserClientRole> findByUserClient(UserClient userClient,
            Pageable pageable);

    /**
     * Reload an userClientUserClientRole with passed in primary key
     * 
     * @param UserClientUserClientRole
     *            to reload
     * @return one UserClientUserClientRole
     */
    UserClientUserClientRole reload(
            UserClientUserClientRole userClientUserClientRole);

    /**
     * Delete an userClientUserClientRole
     * 
     * @param UserClientUserClientRole
     *            to delete
     */
    void delete(UserClientUserClientRole userClientUserClientRole);
}
