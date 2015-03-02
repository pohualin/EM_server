package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User client secret question persistence
 *
 */
public interface UserClientSecretQuestionResponsePersistence {

   
    /**
     * 
     * @param userClient the user client
     * @param pageable the pagination specification
     * @return page user client secret question response
     */
    Page<UserClientSecretQuestionResponse> findByUserClient (UserClient userClient, Pageable pageable);
    
    /**
     * Save or update the secret question response
     * @param questionResponse the user client secret question response
     * @return user client secret question response
     */
    UserClientSecretQuestionResponse saveOrUpdate (UserClientSecretQuestionResponse questionResponse);
  
    /**
     * Reload the user client secret question response 
     * @param userClientSecretQuestion the user client secret question 
     * @return user client secret question response
     */
    UserClientSecretQuestionResponse reload(UserClientSecretQuestionResponse userClientSecretQuestion);
}    

