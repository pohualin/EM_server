package com.emmisolutions.emmimanager.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.UserClientUserClientTeamRoleSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;

/**
 * The UserClientUserClientTeamRole Service
 */
public interface UserClientUserClientTeamRoleService {

    /**
     * Associate a set of UserClientUserClientTeamRole
     * 
     * @return a set of associated UserClientUserClientTeamRole
     */
    Set<UserClientUserClientTeamRole> associate(
	    List<UserClientUserClientTeamRole> userClientUserClientTeamRoles);

    /**
     * Delete passed in UserClientUserClientTeamRole
     * 
     * @param userClientUserClientTeamRole
     *            to delete
     */
    void delete(UserClientUserClientTeamRole userClientUserClientTeamRole);

    /**
     * Delete all UserClientUserClientTeamRole by UserClientId and
     * UserClientTeamRole
     * 
     * @param userClient
     * @param userClientTeamRole
     */
    void delete(UserClient userClient, UserClientTeamRole userClientTeamRole);

    /**
     * Find a Page of existing UserClientUserClientTeamRole by UserClient and
     * UserClientTeamRole
     * 
     * @param userClient
     * @param userClientTeamRole
     * @param pageable
     * @return a Page of existing UserClientUserClientTeamRole
     */
    Page<UserClientUserClientTeamRole> findByUserClientAndUserClientTeamRole(
	    UserClient userClient, UserClientTeamRole userClientTeamRole,
	    Pageable pageable);

    /**
     * Return an existing List of UserClientUserClientTeamRole by userClient and
     * teams
     * 
     * @param userClient
     * @param teams
     * @return An existing List of UserClientUserClientTeamRole by userClient
     *         and teams
     */
    List<UserClientUserClientTeamRole> findExistingByUserClientInTeams(
	    UserClient userClient, List<Team> teams);

    /**
     * Find a Page of possible UserClientUserClientTeamRole by filter
     * 
     * @param filter
     * @param pageable
     * @return a Page of possible UserClientUserClientTeamRole
     */
    Page<UserClientUserClientTeamRole> findPossible(
	    UserClientUserClientTeamRoleSearchFilter filter, Pageable pageable);

    /**
     * Reload UserClientUserClientTeamRole
     * 
     * @param userClientUserClientTeamRole
     *            to reload
     * @return UserClientUserClientTeamRole
     */
    UserClientUserClientTeamRole reload(
	    UserClientUserClientTeamRole userClientUserClientTeamRole);
}
