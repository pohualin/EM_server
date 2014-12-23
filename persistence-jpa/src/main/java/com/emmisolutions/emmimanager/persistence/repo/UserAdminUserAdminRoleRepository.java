package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;

/**
 * Spring Data repo for User Entities
 */
public interface UserAdminUserAdminRoleRepository extends JpaRepository<UserAdminUserAdminRole, Long>, JpaSpecificationExecutor<UserAdminUserAdminRole> {

    /**
     * Deletes all UserAdminUserAdminRoleRepository for a give userAdmin
     *
     * @param userAdmin to delete
     */
    long deleteAllByUserAdmin(UserAdmin userAdmin);
}
