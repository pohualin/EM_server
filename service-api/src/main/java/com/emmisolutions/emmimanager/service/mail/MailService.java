package com.emmisolutions.emmimanager.service.mail;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
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
     * @param trackingUrl   the tracking image url
     */
    void sendActivationEmail(UserClient user, String activationUrl, String trackingUrl);


    /**
     * Sends an validation email to a user
     *
     * @param user          to send an validation to
     * @param validationUrl url for validation
     * @param trackingUrl   the tracking image url
     */
    void sendValidationEmail(UserClient user, String validationUrl, String trackingUrl);

    /**
     * Sends an password reset email to a user
     *
     * @param user             to send an activation to
     * @param passwordResetUrl for the links to actually activate
     * @param trackingUrl      the tracking image url
     */
    void sendPasswordResetEmail(UserClient user, String passwordResetUrl, String trackingUrl);

    /**
     * Sends a password reset to an email that is not yet validated
     * in our system.
     *
     * @param userClient  to send to
     * @param trackingUrl the tracking image url
     */
    void sendInvalidAccountPasswordResetEmail(UserClient userClient, String trackingUrl);

    /**
     * Sends an email to the user if the user has an email that
     * informs them that their password has been changed.
     *
     * @param userClient  to send to
     * @param trackingUrl the tracking image url
     */
    void sendPasswordChangeConfirmationEmail(UserClient userClient, String trackingUrl);

    /**
     * Sends an password reset not enabled email to a user
     *
     * @param userClient  to send to
     * @param trackingUrl the tracking image url
     */
    void sendPasswordResetNotEnabled(UserClient userClient, String trackingUrl);

    /**
     * Sends a patient reminder email for a scheduled program
     *
     * @param scheduledProgram to remind about
     * @param startEmmiUrl     url to view the program
     * @param trackingUrl      the tracking image url
     */
    void sendPatientScheduledProgramReminderEmail(ScheduledProgram scheduledProgram, String startEmmiUrl, String trackingUrl);
}
