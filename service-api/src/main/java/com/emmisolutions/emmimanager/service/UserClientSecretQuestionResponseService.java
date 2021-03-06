package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User client secret question response service
 */
public interface UserClientSecretQuestionResponseService {

    /**
     * List all secret questions
     *
     * @param pageable the pagination specification
     * @return page SecretQuestion
     */
    Page<SecretQuestion> list(Pageable pageable);

    /**
     * Reloads the question response
     *
     * @param questionResponse the question response
     * @return UserClientSecretQuestionResponse
     */
    UserClientSecretQuestionResponse reload(UserClientSecretQuestionResponse questionResponse);

    /**
     * find question response by user client
     *
     * @param userClient the user client
     * @param pageable   the pagination specification
     * @return page of UserClientSecretQuestionResponse
     */
    Page<UserClientSecretQuestionResponse> findByUserClient(UserClient userClient, Pageable pageable);

    /**
     * find question response by reset password token
     *
     * @param resetToken the reset password token
     * @param pageable   the pagination specification
     * @return page of UserClientSecretQuestionResponse
     */
    Page<UserClientSecretQuestionResponse> findSecretQuestionToken(String resetToken, Pageable pageable);

    /**
     * Validate user input security response with database response
     *
     * @param resetToken       the password reset token
     * @param questionResponse user input response
     * @return boolean is input response match or not
     */
    boolean validateSecurityResponse(String resetToken, UserClientSecretQuestionResponse questionResponse);

    /**
     * Save or update the secret question response
     *
     * @param questionResponse the secret question response
     * @return User client secret question response
     */
    UserClientSecretQuestionResponse saveOrUpdate(UserClientSecretQuestionResponse questionResponse);

    /**
     * Save or update user client for the secret question flag
     *
     * @param userClient user client
     * @return UserClient a user client
     */
    UserClient saveOrUpdateUserClient(UserClient userClient);
    
    /**
     * Validate security question response
     * 
     * @param questionResponse to validate
     * @return true if response matches, otherwise false
     */
    boolean validateSecurityResponse(UserClientSecretQuestionResponse questionResponse);

}
