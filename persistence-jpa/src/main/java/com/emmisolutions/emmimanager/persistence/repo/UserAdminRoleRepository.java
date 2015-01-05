package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;

/**
 * Spring Data repo for User Entities
 */
public interface UserAdminRoleRepository extends JpaRepository<UserAdminRole, Long>, JpaSpecificationExecutor<UserAdminRole> {

    /**
     * Find all user admin roles excluding the system roles
     *
     * @return Page<UserAdminRole> or null
     */
    @Query("select ur from UserAdminRole ur where ur.systemRole = false")
    Page<UserAdminRole> findAllWithoutSystem(Pageable pageable);
}
