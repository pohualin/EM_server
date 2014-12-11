package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;

/**
 * The UserClientUserClientRole Service
 */
public interface UserClientUserClientRoleService {

    /**
     * Creates a new UserClientUserClientRole with the passed
     * UserClientUserClientRole object.
     *
     * @param UserClientUserClientRole
     *            to create
     * @return the created UserClientUserClientRole
     */
    UserClientUserClientRole create(
	    UserClientUserClientRole userClientUserClientRole);

    /**
     * @param userClientId
     *            to be used
     * @param pageable
     *            to be used
     * @return a page of UserClientUserClientRole having passed in userClientId
     */
    Page<UserClientUserClientRole> findByUserClient(Long userClientId,
	    Pageable pageable);

    /**
     * @param userClientUserClientId
     *            to find
     * @return one UserClientUserClientRole with userClientUserClientId
     */
    UserClientUserClientRole reload(Long userClientUserClientId);

    /**
     * @param userClientUserClientId
     *            to delete
     */
    void delete(Long userClientUserClientId);
}
