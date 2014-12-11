package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;

/**
 * Spring Data repo for UserClientUserClientTeamRole Entities
 */
public interface UserClientUserClientTeamRoleRepository extends
	JpaRepository<UserClientUserClientTeamRole, Long>,
	JpaSpecificationExecutor<UserClientUserClientTeamRole> {

    public Page<UserClientUserClientTeamRole> findByUserClient(
	    UserClient userClient, Pageable pageable);
}
