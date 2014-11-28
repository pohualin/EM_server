package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for UserClientTeamPermission Entities
 */
public interface UserClientTeamPermissionRepository extends JpaRepository<UserClientTeamPermission, Long>, JpaSpecificationExecutor<UserClientTeamPermission> {


}
