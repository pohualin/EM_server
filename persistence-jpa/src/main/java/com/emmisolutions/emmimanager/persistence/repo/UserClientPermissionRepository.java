package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for UserClientPermission Entities
 */
public interface UserClientPermissionRepository extends JpaRepository<UserClientPermission, Long>, JpaSpecificationExecutor<UserClientPermission> {


}
