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

    public void deleteAllByUserClientIdAndUserClientTeamRoleId(
	    Long userClientId, Long userClientTeamRoleId);

    public Page<UserClientUserClientTeamRole> findByUserClientIdAndUserClientTeamRoleId(
	    Long userClientId, Long userClientTeamRoleId, Pageable pageable);

    public List<UserClientUserClientTeamRole> findByUserClientIdAndTeamIn(
	    Long userClientId, List<Team> teams);
}
