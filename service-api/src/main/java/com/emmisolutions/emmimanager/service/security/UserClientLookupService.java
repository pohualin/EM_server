package com.emmisolutions.emmimanager.service.security;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * Find UserClient objects by alternative means
 */
public interface UserClientLookupService {

    /**
     * Finds a UserClient by its activation code
     *
     * @param activationKey to find by
     * @return a UserClient or null
     */
    UserClient findByActivationKey(String activationKey);

    /**
     * Finds a UserClient by its password reset token
     *
     * @param resetToken to find by
     * @return a UserClient or null
     */
    UserClient findByResetToken(String resetToken);

    /**
     * Finds a UserClient by its validation token
     *
     * @param validationToken to find by
     * @return a UserClient or null
     */
    UserClient findByValidationToken(String validationToken);
}
