package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Client persistence class
 */
public interface UserClientSecretQuestionResponsePersistence {

   
    /**
     * Retrieve questions based on user client id
     * @param User client id and pageable Object to load
     * @return pages of secret questions or null
     */
    Page<UserClientSecretQuestionResponse> findByUserClient (UserClient userClient, Pageable pageable);
    
    /**
     * Save or update questions 
     *
     * @param UserClientSecretQuestionResponse Oject to save
     * @return UserClientSecretQuestionResponse with newly created ID 
     */
    UserClientSecretQuestionResponse saveOrUpdate (UserClientSecretQuestionResponse questionResponse);
  
    /**
     * Retrieve one question response
     * @param UserClient id
     * @return one Secret Question Response if exist 
     */
    UserClientSecretQuestionResponse reload(Long id);
    
    /**
     * Retrieve one question response
     * @param User client id and question id
     * @return one secret questions if exist 
     */
    UserClientSecretQuestionResponse findByUserClientIdAndSecretQuestionId(Long userClientId, Long questionId);
    
}
