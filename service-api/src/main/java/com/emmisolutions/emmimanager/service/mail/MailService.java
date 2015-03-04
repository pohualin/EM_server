package com.emmisolutions.emmimanager.service.mail;

import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * Mail Service
 */
public interface MailService {

    /**
     * Sends an activation email to a user
     *
     * @param user          to send an activation to
     * @param activationUrl for the links to actually activate
     */
    void sendActivationEmail(UserClient user, String activationUrl);


    /**
     * Sends an validation email to a user
     *
     * @param user          to send an validation to
     * @param validationUrl url for validation
     */
    void sendValidationEmail(UserClient user, String validationUrl);

    /**
     * Sends an password reset email to a user
     *
     * @param user             to send an activation to
     * @param passwordResetUrl for the links to actually activate
     */
    void sendPasswordResetEmail(UserClient user, String passwordResetUrl);

    /**
     * Sends a password reset to an email that is not yet validated
     * in our system.
     *
     * @param userClient to send to
     */
    void sendInvalidAccountPasswordResetEmail(UserClient userClient);

    /**
     * Sends an email to the user if the user has an email that
     * informs them that their password has been changed.
     *
     * @param userClient to send to
     */
    void sendPasswordChangeConfirmationEmail(UserClient userClient);
    
    /**
     * Sends an password reset not enabled email to a user
     * 
     * @param userClient
     *            to send to
     */
    void sendPasswordResetNotEnabled(UserClient userClient);
}
