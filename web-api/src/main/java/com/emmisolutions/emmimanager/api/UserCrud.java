package com.emmisolutions.emmimanager.api;

import com.emmisolutions.emmimanager.model.User;

/**
 * The user CRUD interface
 */
public interface UserCrud {

    User getUser();

    void updateUser(User user);

    User save(User user);

    void delete(User user);

}
