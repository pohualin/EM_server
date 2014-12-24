package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Persistence API for UserClientUserClientRole.
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

    /**
     * @param userClient
     *            to use
     * @param pageable
     *            to use
     * @return a page of UserClientUserClientRole having UserClient
     */
    Page<UserClientUserClientRole> findByUserClient(UserClient userClient,
            Pageable pageable);

    /**
     * Find the UserClientUserClientRole by userClientUserClientId
     * 
     * @param userClientUserClientId
     *            to use
     * @return UserClientUserClient with userClientUserClientId
     */
    UserClientUserClientRole reload(Long userClientUserClientId);

    /**
     * Delete an UserClientUserClientRole with userClientUserClientId
     * 
     * @param userClientUserClientId
     *            to use
     */
    void delete(Long userClientUserClientId);
}
