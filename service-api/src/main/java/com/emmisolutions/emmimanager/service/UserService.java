package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.User;

/**
 * The User Service
 */
public interface UserService {

    /**
     * Creates a new user or updates an existing user with the passed User object.
     *
     * @param user to save
     * @return the saved user
     */
    User save(User user);

    /**
     * Fetch the currently logged in user.
     *
     * @return the User
     */
    User loggedIn();

}
