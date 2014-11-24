package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * The User Client Service
 */
public interface UserClientService {

    /**
     * Creates a new user client or updates an existing user client with the passed UserClient object.
     *
     * @param user to save
     * @return the saved user
     */
    UserClient save(UserClient user);

}
