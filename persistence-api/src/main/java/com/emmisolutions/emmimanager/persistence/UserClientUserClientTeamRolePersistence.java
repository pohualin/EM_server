package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;

/**
 * Persistence API for UserClientUserClientTeamRolePersistence
 */
public interface UserClientUserClientTeamRolePersistence {

    /**
     * Saves or updates the UserClientUserClientTeamRole object
     *
     * @param userClientUserClientTeamRole
     *            the UserClientUserClientTeamRole to save or create
     * @return the saved UserClientUserClientTeamRole
     */
    UserClientUserClientTeamRole saveOrUpdate(
	    UserClientUserClientTeamRole userClientUserClientTeamRole);

    /**
     * @param userClient
     *            to use
     * @param pageable
     *            to use
     * @return a page of UserClientUserClientTeamRole having UserClient
     */
    Page<UserClientUserClientTeamRole> findByUserClient(UserClient userClient,
	    Pageable pageable);

    /**
     * @param userClientUserClientTeamRoleId
     *            to reload
     * @return UserClientUserClientTeamRole found with
     *         userClientUserClientTeamRoleId
     */
    UserClientUserClientTeamRole reload(Long userClientUserClientTeamRoleId);

    /**
     * @param userClientUserClientTeamRoleId
     */
    void delete(Long userClientUserClientTeamRoleId);
}
