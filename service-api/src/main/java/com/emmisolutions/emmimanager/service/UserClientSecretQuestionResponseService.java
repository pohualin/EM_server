package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * User client secret question response service
 *
 */
public interface UserClientSecretQuestionResponseService {
    

    /**
     * 
     * @param pageable
     * @return page of secret question
     */
   Page<SecretQuestion> list(Pageable pageable);
    
   /**
    * 
    * @param questionResponse
    * @return User client secret question response
    */
   UserClientSecretQuestionResponse reload(UserClientSecretQuestionResponse questionResponse);
    
    
    /**
     * 
     * @param userClient
     * @param pageable
     * @return page of user client secret question response
     */
   Page<UserClientSecretQuestionResponse> findByUserClient (UserClient userClient, Pageable pageable);
    
     
    /**
     * 
     * @param questionResponse
     * @return User client secret question response
     */
   UserClientSecretQuestionResponse saveOrUpdate(UserClientSecretQuestionResponse questionResponse);
}
