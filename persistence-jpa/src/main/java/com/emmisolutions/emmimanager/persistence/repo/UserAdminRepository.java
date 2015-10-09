package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repo for User Entities
 */
@SuppressWarnings("unchecked")
public interface UserAdminRepository extends JpaRepository<UserAdmin, Long>, JpaSpecificationExecutor<UserAdmin> {

    /**
     * Find a user by login and eagerly fetch all of the permissions
     *
     * @param login this is case sensitive (on purpose)
     * @return User or null
     */
    @Cacheable(value = "adminFetchWithFullPermissions")
    @Query("select u from UserAdmin u left join fetch u.roles ur left join fetch ur.userAdminRole r left join fetch r.permissions p where u.login = :login")
    UserAdmin fetchWithFullPermissions(@Param("login") String login);

    @Caching(evict = {
            @CacheEvict(value = "adminFetchWithFullPermissions", allEntries = true)
    })
    @Override
    UserAdmin save(UserAdmin userAdmin);
}
