package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.User;

import java.util.List;

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

    User reload(String login);

    User fetchUserWillFullPermissions(String login);

    List<User> findAllContractOwners();

}
