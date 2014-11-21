package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;

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
    UserAdmin save(UserAdmin user);

    /**
     * Fetch the currently logged in user.
     *
     * @return the User
     */
    UserAdmin loggedIn();

}
