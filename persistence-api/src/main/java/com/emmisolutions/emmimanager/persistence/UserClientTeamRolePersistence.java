package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Persistence API for UserClient roles and permissions.
 */
public interface UserClientTeamRolePersistence {

    /**
     * Find a page of roles for a client user
     *
     * @param clientId for which to find the roles
     * @param page     specification
     * @return a page of UserClientTeamRole objects
     */
    Page<UserClientTeamRole> find(long clientId, Pageable page);

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
     * Removes a UserClientTeamRole by id
     *
     * @param id to remove
     */
    void remove(Long id);

    /**
     * load all possible team level permissions
     *
     * @return the set of permissions
     */
    Set<UserClientTeamPermission> loadPossiblePermissions();

    /**
     * Load all permissions for a UserClientRole
     *
     * @param userClientTeamRole to load for
     * @return Set of UserClientTeamPermission or empty Set never null
     */
    Set<UserClientTeamPermission> permissionsFor(UserClientTeamRole userClientTeamRole);

}
