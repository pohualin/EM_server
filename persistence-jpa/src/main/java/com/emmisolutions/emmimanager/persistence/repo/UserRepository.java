package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repo for User Entities
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Find a user by the login
     *
     * @param login case insensitive comparison
     * @return User or null
     */
    User findByLoginIgnoreCase(String login);


    /**
     * Find a user by login and eagerly fetch all of the permissions
     *
     * @param login this is case sensitive (on purpose)
     * @return User or null
     */
    @Query("select u from User u left join fetch u.roles r left join fetch r.permissions p where u.login = :login")
    User fetchWithFullPermissions(@Param("login") String login);
}
