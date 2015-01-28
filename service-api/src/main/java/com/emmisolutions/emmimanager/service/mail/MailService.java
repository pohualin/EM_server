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

}
