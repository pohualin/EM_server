package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Service layer for UserClientTeamRole objects
 */
public interface UserClientTeamRoleService {

    /**
     * Find a page of roles for a client user
     *
     * @param client for which to find the roles
     * @param page     specification
     */
    Page<UserClientTeamRole> find(Client client, Pageable page);

    /**
     * Create/Update a user client role
     *
     * @param userClientTeamRole to be saved
     * @return the saved user client role
     */
    UserClientTeamRole save(UserClientTeamRole userClientTeamRole);

    /**
     * Reloads a UserClientTeamRole from persistence
     *
     * @param userClientTeamRole to reload
     * @return the persistent UserClientTeamRole object or null if the userClientTeamRole is null or not found
     */
    UserClientTeamRole reload(UserClientTeamRole userClientTeamRole);

    /**
     * Load all permissions for a role
     *
     * @param userClientTeamRole to load them for
     * @return the set of permissions
     */
    Set<UserClientTeamPermission> loadAll(UserClientTeamRole userClientTeamRole);

    /**
     * Creates a new UserClientTeamRole
     *
     * @param userClientTeamRole to create
     * @return new role
     */
    UserClientTeamRole create(UserClientTeamRole userClientTeamRole);

    /**
     * Remove a user client role
     *
     * @param userClientTeamRole to be removed
     */
    void remove(UserClientTeamRole userClientTeamRole);
}
