package com.emmisolutions.emmimanager.service;


import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.springframework.transaction.annotation.Transactional;

public interface UserClientValidationEmailService {
    /**
     * Amount of hours that validation tokens are valid for after creation
     */
    int VALIDATION_TOKEN_HOURS_VALID = 24;

    /**
     * Add a validation token to the UserClient
     *
     * @param userClient on which to add the validation token
     * @return the saved UserClient
     */
    public UserClient addValidationTokenTo(UserClient userClient);

    /**
     * Validate email token
     * @param validationEmailToken token to validate
     * @return
     */
    @Transactional
    UserClient validateEmailToken(String validationEmailToken);
}
