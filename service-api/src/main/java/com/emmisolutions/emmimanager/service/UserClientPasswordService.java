package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;

/**
 * Administrative Service API for UserClient password management
 */
public interface UserClientPasswordService {

    /**
     * Amount of hours that reset tokens are valid for after creation
     */
    int RESET_TOKEN_HOURS_VALID = 24;

    /**
     * Update the UserClient password.
     *
     * @param user           the user to be updated
     * @param setCredentialNonExpire to set
     * @return the updated UserClient
     */
    UserClient updatePassword(UserClient user, boolean setCredentialNonExpired);

    /**
     * Updates an UserClient password from an expired password change request
     *
     * @param expiredPasswordChangeRequest specifies the update
     * @return the updated user client
     */
    UserClient changeExpiredPassword(ExpiredPasswordChangeRequest expiredPasswordChangeRequest);

    /**
     * Update an UserClient with sent in new password
     *
     * @param changePasswordRequest to change the password
     * @return the updated user client
     */
    UserClient changePassword(ChangePasswordRequest changePasswordRequest);

    /**
     * Encodes whatever password is currently on the UserClient
     *
     * @param userClient to find the password
     * @return UserClient with both password and salt populated
     */
    UserClient encodePassword(UserClient userClient);

    /**
     * Update a user's password with a reset password request
     *
     * @param resetPasswordRequest request to reset the password
     * @return the UserClient with a reset password
     */
    UserClient resetPassword(ResetPasswordRequest resetPasswordRequest);

    /**
     * Add a reset token to the UserClient
     *
     * @param userClient on which to add the reset token
     * @return the saved UserClient
     */
    UserClient addResetTokenTo(UserClient userClient);

    /**
     * A user has forgotten their password. This method creates
     * a reset password token if the UserClient has a validated
     * email.
     *
     * @param email to look up the user
     * @return the updated UserClient with a reset token or null
     */
    UserClient forgotPassword(String email);

    /**
     * Expires the reset token but does not remove it.
     *
     * @param userClient on which to expire token
     * @return the updated UserClient
     */
    UserClient expireResetToken(UserClient userClient);

    /**
     * Finds a password configuration for a particular reset token
     * or returns the default password configuration
     *
     * @param resetToken use to find a client
     * @return a password configuration for the token, never null
     */
    ClientPasswordConfiguration findPasswordPolicyUsingResetToken(String resetToken);

    /**
     * Finds a password configuration for a particular activation token
     * or returns the default password configuration
     *
     * @param resetToken use to find a client
     * @return a password configuration for the token, never null
     */
    ClientPasswordConfiguration findPasswordPolicyUsingActivationToken(String resetToken);

    /**
     * Set password expiration time to userClient
     *
     * @param userClient to set
     * @return an userClient with password expiration time set
     */
    UserClient updatePasswordExpirationTime(UserClient userClient);
}
