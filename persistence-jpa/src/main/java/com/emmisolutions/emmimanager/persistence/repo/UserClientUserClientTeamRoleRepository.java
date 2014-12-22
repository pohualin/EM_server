package com.emmisolutions.emmimanager.persistence.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;

/**
 * Spring Data repo for UserClientUserClientTeamRole Entities
 */
public interface UserClientUserClientTeamRoleRepository extends
		JpaRepository<UserClientUserClientTeamRole, Long>,
		JpaSpecificationExecutor<UserClientUserClientTeamRole> {

	/**
	 * Delete all UserClientUserClientTeamRoles with given userClientId and
	 * userClientTeamRoleId
	 * 
	 * @param userClientId
	 *            to use
	 * @param userClientTeamRoleId
	 *            to use
	 */
	public void deleteAllByUserClientIdAndUserClientTeamRoleId(
			Long userClientId, Long userClientTeamRoleId);

	/**
	 * Find a page of UserClientUserClientTeamRole with given userClientId and
	 * userClientTeamRoleId
	 * 
	 * @param userClientId
	 *            to use
	 * @param userClientTeamRoleId
	 *            to use
	 * @param pageable
	 *            to use
	 * @return a page of UserClientUserClientTeamRole
	 */
	public Page<UserClientUserClientTeamRole> findByUserClientIdAndUserClientTeamRoleId(
			Long userClientId, Long userClientTeamRoleId, Pageable pageable);

	/**
	 * Find a list of UserClientUserClientTeamRole with given userClientId and
	 * teams
	 * 
	 * @param userClientId
	 *            to use
	 * @param teams
	 *            to use
	 * @return a list of UserClientUserClientTeamRole
	 */
	public List<UserClientUserClientTeamRole> findByUserClientIdAndTeamIn(
			Long userClientId, List<Team> teams);
}
