package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repo for User Entities
 */
public interface UserRepository extends JpaRepository<UserAdmin, Long>, JpaSpecificationExecutor<UserAdmin> {

    /**
     * Find a user by the login
     *
     * @param login case insensitive comparison
     * @return User or null
     */
    UserAdmin findByLoginIgnoreCase(String login);


    /**
     * Find a user by login and eagerly fetch all of the permissions
     *
     * @param login this is case sensitive (on purpose)
     * @return User or null
     */
    @Query("select u from UserAdmin u left join fetch u.roles ur left join fetch ur.userAdminRole r left join fetch r.permissions p where u.login = :login")
    UserAdmin fetchWithFullPermissions(@Param("login") String login);
}
