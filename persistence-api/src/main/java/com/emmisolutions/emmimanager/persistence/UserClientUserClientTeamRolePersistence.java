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
     *            to delete
     */
    void delete(Long userClientUserClientTeamRoleId);

    /**
     * Delete all UserClientUserClientTeamRole by userClientId and
     * userClientTeamRoleId
     * 
     * @param userClientId
     *            to lookup and delete
     * @param userClientTeamRoleId
     *            to lookup and delete
     */
    void delete(Long userClientId, Long userClientTeamRoleId);

    /**
     * Find a list of existing UserClientUserClientTeamRole by userClientId and
     * teams
     * 
     * @param userClientId
     *            to find
     * @param teams
     *            to use
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
     *            to find
     * @param userClientTeamRoleId
     *            to find
     * @param pageable
     *            to use
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
