package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * A sample persistence interface.
 */
public interface UserPersistence {

    /**
     * Saves or updates the User object
     *
     * @param user the user to save or create
     * @return the saved user
     */
    User saveOrUpdate(User user);

    /**
     * Pulls the User from the database using the login
     *
     * @param login case insensitive search
     * @return User or null if one is not found
     */
    User reload(String login);

    /**
     * Retrieves a user by login and eagerly loads all permissions
     *
     * @param login case insensitive search
     * @return a User or null
     */
    User fetchUserWillFullPermissions(String login);

    /**
     * Finds a page of Users that are eligible to be contract owners
     * @param pageable the specification to fetch
     * @return a page of User objects
     */
    Page<User> listPotentialContractOwners(Pageable pageable);
}
