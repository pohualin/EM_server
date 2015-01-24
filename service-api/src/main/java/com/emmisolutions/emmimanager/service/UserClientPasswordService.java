package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;

/**
 * Administrative Service API for UserClient password management
 */
public interface UserClientPasswordService {

    /**
     * Update the UserClient password and expire it at the same time.
     *
     * @param user the user to be updated
     */
    void updatePassword(UserClient user);

    /**
     * Updates a UserClient password from an expired password change request
     *
     * @param expiredPasswordChangeRequest specifies the update
     */
    void changeExpiredPassword(ExpiredPasswordChangeRequest expiredPasswordChangeRequest);
}
