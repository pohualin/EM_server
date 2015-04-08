package com.emmisolutions.emmimanager.web.rest.client.configuration.security;


import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;

import org.springframework.security.core.Authentication;
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
     * @param resetPasswordObject ResetPasswordRequest object
     * @param authentication the authenticated user
     * @return true if questions and responses are valid, false if it doesn't
     */
    public boolean isSecurityReponseValid(Object resetPasswordObject, Authentication authentication) {
        boolean isValid = false;
        ResetPasswordRequest resetPassword = (ResetPasswordRequest) resetPasswordObject;
                
        if ((resetPasswordObject != null) && 
            (resetPassword.getUserClientSecretQuestionResponse() != null)) {
             isValid = userClientSecretQuestionResponseService.validateSecurityResponse(resetPassword.getResetToken(), resetPassword.getUserClientSecretQuestionResponse());
        }
        return isValid;
    }

}
