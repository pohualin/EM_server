package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * The User Client Service
 */
public interface UserClientService {

    /**
     * Creates a new user client user with the passed UserClient object.
     *
     * @param user to create
     * @return the created user
     */
    UserClient create(UserClient user);
    
    /**
     * Update an existing client user with the passed UserClient object.
     *
     * @param user to update
     * @return the created user
     */
    UserClient update(UserClient user);

}
