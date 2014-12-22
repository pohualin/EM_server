package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.UserSearchFilter;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;

/**
 * The User Service
 */
public interface UserService {

    /**
     * Creates a new user admin or updates an existing user with the passed User object.
     *
     * @param user to save
     * @return the saved user
     */
    UserAdmin save(UserAdmin user);

    /**
     * Fetch the currently logged in user.
     *
     * @return the User
     */
    UserAdmin loggedIn();

    /**
     * Creates a new user 
     *
     * @param user to save
     * @return the saved user
     */
	User create(User user);

    /**
     * Reloads a User from persistent storage
     *
     * @param user to reload
     * @return the reloaded user
     */
	User reload(User user);
	
    /**
     * Get a page of user objects.
     *
     * @param page             to retrieve
     * @param userSearchFilter filtered by
     * @return a page of users objects
     */
	Page<User> list(Pageable page, UserSearchFilter userSearchFilter);

}
