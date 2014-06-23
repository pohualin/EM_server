package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.User;

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

}
