package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLoginIgnoreCase(String login);

    @Query("select u from User u left join fetch u.roles r left join fetch r.permissions p where u.login = :login")
    User fetchWithFullPermissions(@Param("login") String login);
}
