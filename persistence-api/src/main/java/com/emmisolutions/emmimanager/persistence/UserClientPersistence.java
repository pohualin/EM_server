package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * A sample persistence interface.
 */
public interface UserClientPersistence {

    /**
     * Saves or updates the User object
     *
     * @param user the user to save or create
     * @return the saved user
     */
    UserClient saveOrUpdate(UserClient user);

}
