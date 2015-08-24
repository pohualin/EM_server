package com.emmisolutions.emmimanager.service;


import com.emmisolutions.emmimanager.model.user.client.UserClient;

public interface UserClientValidationEmailService {
    /**
     * Amount of hours that validation tokens are valid for after creation
     */
    int VALIDATION_TOKEN_HOURS_VALID = 24;

    /**
     * Add a validation token to the UserClient
     *
     * @param userClient on which to add the validation token
     * @return the saved UserClient related to the token
     */
    UserClient addValidationTokenTo(UserClient userClient);

    /**
     * Validate email token and remove it from the user
     *
     * @param validationEmailToken token to validate
     * @return the updated user client attached to the token
     */
    UserClient validateEmailToken(String validationEmailToken);
}
