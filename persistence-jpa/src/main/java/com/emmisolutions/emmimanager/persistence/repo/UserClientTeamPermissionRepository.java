package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Spring Data repo for UserClientTeamPermission Entities
 */
public interface UserClientTeamPermissionRepository extends JpaRepository<UserClientTeamPermission, Long>, JpaSpecificationExecutor<UserClientTeamPermission> {

    /**
     * Find all permissions for a UserClientRole id
     *
     * @param id of the UserClientRole
     * @return a set of UserClientPermission objects
     */
    Set<UserClientTeamPermission> findAllByUserClientTeamRolesId(Long id);
}
