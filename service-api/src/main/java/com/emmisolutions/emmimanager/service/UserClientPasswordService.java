package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * Administrative Service API for UserClient password management
 */
public interface UserClientPasswordService {

    /**
     * Update the UserClient password.
     *
     * @param user the user to be updated
     */
    void updatePassword(UserClient user);
}
