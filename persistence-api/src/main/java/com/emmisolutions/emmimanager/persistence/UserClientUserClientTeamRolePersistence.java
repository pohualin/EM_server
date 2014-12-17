package com.emmisolutions.emmimanager.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;

/**
 * Persistence API for UserClientUserClientTeamRole
 */
public interface UserClientUserClientTeamRolePersistence {

    /**
     * Delete UserClientUserClientTeamRole with userClientUserClientTeamRoleId
     * 
     * @param userClientUserClientTeamRoleId
     */
    void delete(Long userClientUserClientTeamRoleId);

    /**
     * Delete all UserClientUserClientTeamRole by userClientId and
     * userClientTeamRoleId
     * 
     * @param userClientId
     * @param userClientTeamRoleId
     */
    void delete(Long userClientId, Long userClientTeamRoleId);

    /**
     * Find a list of existing UserClientUserClientTeamRole by userClientId and
     * teams
     * 
     * @param userClientId
     * @param teams
     * @return A list of existing UserClientUserClientTeamRole by userClientId
     *         and teams
     */
    List<UserClientUserClientTeamRole> findByUserClientIdAndTeamsIn(
	    Long userClientId, List<Team> teams);

    /**
     * Find a page of existing UserClientUserClientTeamRole by userClientId and
     * userClientTeamRoleId
     * 
     * @param userClientId
     * @param userClientTeamRoleId
     * @param pageable
     * @return a page of existing UserClientUserClientTeamRole
     */
    Page<UserClientUserClientTeamRole> findByUserClientIdAndUserClientTeamRoleId(
	    Long userClientId, Long userClientTeamRoleId, Pageable pageable);

    /**
     * Reload UserClientUserClientTeamRole with userClientUserClientTeamRoleId
     * 
     * @param userClientUserClientTeamRoleId
     *            to reload
     * @return UserClientUserClientTeamRole
     */
    UserClientUserClientTeamRole reload(Long userClientUserClientTeamRoleId);

    /**
     * Saves or updates the UserClientUserClientTeamRole object
     *
     * @param userClientUserClientTeamRole
     *            the UserClientUserClientTeamRole to save or update
     * @return the saved UserClientUserClientTeamRole
     */
    UserClientUserClientTeamRole saveOrUpdate(
	    UserClientUserClientTeamRole userClientUserClientTeamRole);

}
