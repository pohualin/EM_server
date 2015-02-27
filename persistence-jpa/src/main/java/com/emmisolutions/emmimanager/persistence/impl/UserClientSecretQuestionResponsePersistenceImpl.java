package com.emmisolutions.emmimanager.persistence.impl;

import java.util.List;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
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
 * Repo to deal with User Client Secret Question Response persistence.
 */
@Repository
public class UserClientSecretQuestionResponsePersistenceImpl implements
        UserClientSecretQuestionResponsePersistence {

    @Resource
    UserClientSecretQuestionResponseRepository userClientSecretQuestionResponseRepository;
    
    @Resource
    SecretQuestionRepository SecretQuestionRepository;

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
        return userClientSecretQuestionResponseRepository.save(questionResponse);
          
    }

    @Override
    public UserClientSecretQuestionResponse reload(
            Long id) {
       return userClientSecretQuestionResponseRepository.findOne(id);
    }

   
    @Override
    public UserClientSecretQuestionResponse reload(
    	UserClientSecretQuestionResponse userClientSecretQuestion){
        if (userClientSecretQuestion == null || 
    	     userClientSecretQuestion.getUserClient().getId() == null ||
    	     userClientSecretQuestion.getSecretQuestion().getId() == null) {
             return null;
        }
        return userClientSecretQuestionResponseRepository.
        		findByUserClientIdAndSecretQuestionId(userClientSecretQuestion.getUserClient().getId(),
        				userClientSecretQuestion.getSecretQuestion().getId());
    }

   
}
