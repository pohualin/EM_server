package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for User Entities
 */
public interface UserAdminUserAdminRoleRepository extends JpaRepository<UserAdminUserAdminRole, Long>, JpaSpecificationExecutor<UserAdminUserAdminRole> {

    /**
     * Deletes all UserAdminUserAdminRoleRepository for a give userAdmin
     *
     * @param userAdmin to delete
     */
    @Caching(evict = {
            @CacheEvict(value = "adminFetchWithFullPermissions", key = "#p0.login")
    })
    long deleteAllByUserAdmin(UserAdmin userAdmin);
}
