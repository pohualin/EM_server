package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;

/**
 * The UserClientUserClientTeamRole Service
 */
public interface UserClientUserClientTeamRoleService {

    /**
     * Creates a new UserClientUserClientTeamRole with the passed
     * UserClientUserClientTeamRole object.
     *
     * @param userClientUserClientTeamRole
     *            to create
     * @return the created userClientUserClientTeamRole
     */
    UserClientUserClientTeamRole create(
	    UserClientUserClientTeamRole userClientUserClientTeamRole);

    /**
     * @param userClientId
     *            to be used
     * @param pageable
     *            to be used
     * @return a page of UserClientUserClientTeamRole having passed in
     *         userClientId
     */
    Page<UserClientUserClientTeamRole> findByUserClient(Long userClientId,
	    Pageable pageable);

    /**
     * @param userClientUserClientTeamId
     *            to find
     * @return one UserClientUserClientTeamRole with
     *         userClientUserClientTeamRoleId
     */
    UserClientUserClientTeamRole reload(Long userClientUserClientTeamRoleId);

    /**
     * @param userClientUserClientTeamRoleId
     *            to delete
     */
    void delete(Long userClientUserClientTeamRoleId);
}
