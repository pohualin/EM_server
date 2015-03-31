package com.emmisolutions.emmimanager.persistence.impl;

import java.util.List;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.persistence.SecretQuestionPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientSecretQuestionResponsePersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientSecretQuestionResponseRepository;
import com.emmisolutions.emmimanager.persistence.repo.SecretQuestionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 
 * user client secret question response persistence
 *
 */
@Repository
public class UserClientSecretQuestionResponsePersistenceImpl implements
        UserClientSecretQuestionResponsePersistence {

    @Resource
    UserClientSecretQuestionResponseRepository userClientSecretQuestionResponseRepository;
    
    @Resource
    SecretQuestionRepository SecretQuestionRepository;
    
    @Resource
    SecretQuestionPersistence secretQuestionPersistence;
    
    @Resource
    UserClientPersistence userClientPersistence;

    @Override
    public Page<UserClientSecretQuestionResponse> findByUserClient(UserClient userClient, Pageable pageable) {
        Pageable pageToFetch;
        if (pageable == null) {
            pageToFetch = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        } else {
            pageToFetch = pageable;
        }
        return userClientSecretQuestionResponseRepository.findByUserClientId(userClient.getId(), pageToFetch);
       
    }

    @Override
    public UserClientSecretQuestionResponse saveOrUpdate(
            UserClientSecretQuestionResponse questionResponse) {
    	// reload the secret question and user client because the version may have changed
        questionResponse.setSecretQuestion(secretQuestionPersistence.reload(questionResponse.getSecretQuestion()));
        questionResponse.setUserClient(userClientPersistence.reload(questionResponse.getUserClient()));
        return userClientSecretQuestionResponseRepository.save(questionResponse);
          
    }

    @Override
    public UserClientSecretQuestionResponse reload(
    	UserClientSecretQuestionResponse userClientSecretQuestion){
        if (userClientSecretQuestion == null || userClientSecretQuestion.getId() == null) {
             return null;
        }
        return userClientSecretQuestionResponseRepository.findOne(userClientSecretQuestion.getId());
    }
  
}
