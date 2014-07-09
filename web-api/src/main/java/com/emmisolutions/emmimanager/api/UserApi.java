package com.emmisolutions.emmimanager.api;

import com.emmisolutions.emmimanager.model.User;

/**
 * User API accessible from the web.
 */
public interface UserApi {

    User getUser(User user);

    User create(User user);

    User save(User user);

    void delete(User user);

    User loggedIn();

}
