package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Service layer for UserClientRole objects
 */
public interface UserClientRoleService {

    /**
     * Find a page of roles for a client user
     *
     * @param client for which to find the roles
     * @param page     specification
     */
    Page<UserClientRole> find(Client client, Pageable page);

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
     * Load all permissions for a role
     *
     * @param userClientRole to load them for
     * @return the set of permissions
     */
    Set<UserClientPermission> loadAll(UserClientRole userClientRole);

    /**
     * Creates a new UserClientRole
     *
     * @param userClientRole to create
     * @return new role
     */
    UserClientRole create(UserClientRole userClientRole);

    /**
     * Remove a user client role
     *
     * @param userClientRole to be removed
     */
    void remove(UserClientRole userClientRole);

    /**
     * loads reference groups
     *
     * @param page specification
     * @return a page of UserClientReferenceRole objects
     */
    Page<UserClientReferenceRole> loadReferenceRoles(Pageable page);
}
