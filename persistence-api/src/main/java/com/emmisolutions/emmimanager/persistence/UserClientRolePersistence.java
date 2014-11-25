package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Persistence API for UserClient roles and permissions.
 */
public interface UserClientRolePersistence {

    /**
     * Find a page of roles for a client user
     *
     * @param clientId for which to find the roles
     * @param page     specification
     */
    Page<UserClientRole> find(long clientId, Pageable page);

    /**
     * Create/Update a user client role
     *
     * @param userClientRole to be saved
     * @return the saved user client role
     */
    UserClientRole save(UserClientRole userClientRole);

    /**
     * Reloads a UserClientRole from persistence
     *
     * @param userClientRole to reload
     * @return the persistent UserClientRole object or null if the userClientRole is null or not found
     */
    UserClientRole reload(UserClientRole userClientRole);

    /**
     * Removes a UserClientRole by id
     *
     * @param id to remove
     */
    void remove(Long id);
}
