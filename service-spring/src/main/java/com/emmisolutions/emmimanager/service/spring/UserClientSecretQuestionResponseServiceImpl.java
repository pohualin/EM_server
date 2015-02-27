package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.persistence.SecretQuestionPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientSecretQuestionResponsePersistence;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;
import com.emmisolutions.emmimanager.service.UserClientService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * User client secret question response service Impl
 *
 */
@Service
public class UserClientSecretQuestionResponseServiceImpl implements UserClientSecretQuestionResponseService {

    @Resource
    UserClientSecretQuestionResponsePersistence userClientSecretQuestionResponsePersistence;
    
    @Resource
    SecretQuestionPersistence secretQuestionPersistence;
    
    @Resource
    UserClientService userClientService;
    
    @Resource
    UserClientPersistence userClientPersistence;

    @Override
    @Transactional
    public UserClientSecretQuestionResponse saveOrUpdate(
            UserClientSecretQuestionResponse questionResponse) {
        if (questionResponse == null || 
               secretQuestionPersistence.reload(questionResponse.getSecretQuestion()) == null ||
               userClientPersistence.reload(questionResponse.getUserClient()) == null ||
               StringUtils.isBlank(questionResponse.getResponse())) {
            throw new InvalidDataAccessApiUsageException("Question Response and Secret Question cannot be null");
        }
        
        // reload UserClientSecretQuestionResponse
        UserClientSecretQuestionResponse inDb = userClientSecretQuestionResponsePersistence.
                reload(questionResponse);
        
        // which is user_client_id, secret_question_id
        UserClientSecretQuestionResponse toSave = null;
        if (inDb != null){
            // there is currently a response to this question
            inDb.setResponse(questionResponse.getResponse());
            toSave = inDb;
        } else {
            
            // check to see how many responses are currently stored
            if (userClientSecretQuestionResponsePersistence.findByUserClient(questionResponse.getUserClient(), 
                    new PageRequest(0, 10, Sort.Direction.ASC, "id")).getNumberOfElements() >= 2){
                toSave = userClientSecretQuestionResponsePersistence.reload(questionResponse.getId());
                toSave.setResponse(questionResponse.getResponse());
                toSave.setSecretQuestion(questionResponse.getSecretQuestion());
                toSave = questionResponse;
            }
            else{
                toSave = questionResponse;
            }
        }
        
        toSave.setSecretQuestion(secretQuestionPersistence.reload(toSave.getSecretQuestion()));
        toSave.setUserClient(userClientPersistence.reload(toSave.getUserClient()));
        return userClientSecretQuestionResponsePersistence.saveOrUpdate(toSave);
    }
    
    
    /**
     * List all secret questions
     * @param pageable the pagination specification
     * @return page SecretQuestion
     */
    @Override
    @Transactional
    public Page<SecretQuestion> list(Pageable pageable) {
        return secretQuestionPersistence.findAll(pageable);
    }

    /**
     * Reloads the question response 
     * @param questionResponse the question response
     * @return  UserClientSecretQuestionResponse
     */
    @Override
    @Transactional
    public UserClientSecretQuestionResponse reload(
            UserClientSecretQuestionResponse questionResponse) {
        if (questionResponse == null || questionResponse.getId() == null) {
            return null;
        }
        return userClientSecretQuestionResponsePersistence.reload(questionResponse.getId());
    }

   
    @Override
    @Transactional
    public Page<UserClientSecretQuestionResponse> findByUserClient(
            UserClient userClient, Pageable pageable) {
        userClient = userClientService.reload(userClient);
        if (userClient == null || userClient.getId() == null){
            throw new InvalidDataAccessApiUsageException(
                    "UserClient and UserClientId cannot be null");
        }
        return userClientSecretQuestionResponsePersistence
                .findByUserClient(userClient, pageable);
               
    }


}

