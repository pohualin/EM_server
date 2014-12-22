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
	 *            to reload and delete
	 * @param userClientTeamRole
	 *            to reload and delete
	 */
	void delete(UserClient userClient, UserClientTeamRole userClientTeamRole);

	/**
	 * Find a Page of existing UserClientUserClientTeamRole by UserClient and
	 * UserClientTeamRole
	 * 
	 * @param userClient
	 *            to find
	 * @param userClientTeamRole
	 *            to find
	 * @param pageable
	 *            to use
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
	 *            to find
	 * @param teams
	 *            to use
	 * @return An existing List of UserClientUserClientTeamRole by userClient
	 *         and teams
	 */
	List<UserClientUserClientTeamRole> findExistingByUserClientInTeams(
			UserClient userClient, List<Team> teams);

	/**
	 * Find a Page of possible UserClientUserClientTeamRole by filter
	 * 
	 * @param filter
	 *            to use
	 * @param pageable
	 *            to use
	 * @return a Page of possible UserClientUserClientTeamRole
	 */
	Page<UserClientUserClientTeamRole> findPossible(
			UserClientUserClientTeamRoleSearchFilter filter, Pageable pageable);

	/**
	 * Reload UserClientUserClientTeamRole
	 * 
	 * @param userClientUserClientTeamRole
	 *            to reload
	 * @return UserClientUserClientTeamRole reloaded
	 */
	UserClientUserClientTeamRole reload(
			UserClientUserClientTeamRole userClientUserClientTeamRole);
}
