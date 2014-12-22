package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.UserSearchFilter;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;

/**
 * User persistence interface.
 */
public interface UserPersistence {

    /**
     * Saves or updates the UserAdmin object
     *
     * @param user the user to save or create
     * @return the saved user
     */
    UserAdmin saveOrUpdate(UserAdmin user);

    /**
     * Pulls the User from the database using the login
     *
     * @param login case insensitive search
     * @return User or null if one is not found
     */
    UserAdmin reload(String login);

    /**
     * Retrieves a user by login and eagerly loads all permissions
     *
     * @param login case insensitive search
     * @return a User or null
     */
    UserAdmin fetchUserWillFullPermissions(String login);

    /**
     * Finds a page of Users that are eligible to be contract owners
     * @param pageable the specification to fetch
     * @return a page of User objects
     */
    Page<UserAdmin> listPotentialContractOwners(Pageable pageable);

    /**
     * Saves or updates the User object
     *
     * @param user the user to save or create
     * @return the saved user
     */
	User saveOrUpdate(User user);
	
    /**
     * Pulls the User from the database using the Id
     *
     * @param User object
     * @return User or null if one is not found
     */
	User reload(User user);

	/**
     * Find a page of User based on pagable and filter
     *
     * @param pageable contains pageable information
     * @param filter   contains search criteria
     * @return Page<User>
     */
	Page<User> list(Pageable page, UserSearchFilter filter);
}
