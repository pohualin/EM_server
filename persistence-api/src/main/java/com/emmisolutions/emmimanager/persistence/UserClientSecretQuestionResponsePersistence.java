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
     * @param userClient
     * @param pageable
     * @return page user client secret question response
     */
    Page<UserClientSecretQuestionResponse> findByUserClient (UserClient userClient, Pageable pageable);
    
    /**
     * 
     * @param questionResponse
     * @return user client secret question response
     */
    UserClientSecretQuestionResponse saveOrUpdate (UserClientSecretQuestionResponse questionResponse);
  
    /**
     * 
     * @param id
     * @return user client sercet question response
     */
    UserClientSecretQuestionResponse reload(Long id);
    
    /**
     * 
     * @param userClientSecretQuestion
     * @return user client secret question response
     */
    UserClientSecretQuestionResponse reload(UserClientSecretQuestionResponse userClientSecretQuestion);
}    

