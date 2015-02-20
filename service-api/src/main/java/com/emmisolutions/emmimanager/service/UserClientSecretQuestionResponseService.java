package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Client Service API.
 */
public interface UserClientSecretQuestionResponseService {
    

    /**
     * Get the list of secret questions
     * 
     * @param pageable
     *           
     * @return all secret questions
     */
    public Page<SecretQuestion> list(Pageable pageable);
    
    /**
     * Reloads a specific response
     * 
     * @param userClient to reload
     * @return the persistent UserClientSecretQuestionResponse or null
     */
    public UserClientSecretQuestionResponse reload(UserClientSecretQuestionResponse questionResponse);
    
    
    /**
     * Find all responses for a particular UserClient
     * @param userClient and pageable
     * @return Set of UserClientSecretQuestionResponse
     */
    public Page<UserClientSecretQuestionResponse> findByUserClient (UserClient userClient, Pageable pageable);
    
     
    /**
     * Save or update question and response
     *
     * @param UserClientSecretQuestionResponse Oject to save or update
     * @return the list of UserClientSecretQuestionResponse with newly created ID 
     */
    public UserClientSecretQuestionResponse saveOrUpdate(UserClientSecretQuestionResponse questionResponse);
}
