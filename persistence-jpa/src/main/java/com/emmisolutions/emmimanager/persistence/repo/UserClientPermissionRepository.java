package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Spring Data repo for UserClientPermission Entities
 */
public interface UserClientPermissionRepository extends JpaRepository<UserClientPermission, Long>, JpaSpecificationExecutor<UserClientPermission> {

    /**
     * Find all permissions for a UserClientRole id
     *
     * @param id of the UserClientRole
     * @return a set of UserClientPermission objects
     */
    Set<UserClientPermission> findAllByUserClientRolesId(Long id);
}
