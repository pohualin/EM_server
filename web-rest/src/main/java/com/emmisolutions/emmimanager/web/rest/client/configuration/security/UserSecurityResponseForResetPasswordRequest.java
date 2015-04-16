package com.emmisolutions.emmimanager.web.rest.client.configuration.security;


import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Check to see if questions and responses are match in the database.
 */
@Component("resetPasswordSecurityResponse")
public class UserSecurityResponseForResetPasswordRequest {

    @Resource
    UserClientSecretQuestionResponseService userClientSecretQuestionResponseService;

    /**
     * Check if questions and response are authenticated
     *
     * @param resetPassword to check to see if security questions are valid
     * @return true if questions and responses are valid, false if it doesn't
     */
    public boolean isSecurityResponseValid(ResetPasswordRequest resetPassword) {
        boolean isValid = false;
        if (resetPassword != null) {
            if ( resetPassword.getUserClientSecretQuestionResponse() != null) {
                for (UserClientSecretQuestionResponse userClientSecretQuestionResponse :
                        resetPassword.getUserClientSecretQuestionResponse()) {
                    isValid = userClientSecretQuestionResponseService.validateSecurityResponse(
                            resetPassword.getResetToken(), userClientSecretQuestionResponse);
                    if (!isValid) {
                        break;
                    }
                }
            } else {
                isValid = userClientSecretQuestionResponseService.validateSecurityResponse(
                        resetPassword.getResetToken(), null);
            }
        }
        return isValid;
    }

}
