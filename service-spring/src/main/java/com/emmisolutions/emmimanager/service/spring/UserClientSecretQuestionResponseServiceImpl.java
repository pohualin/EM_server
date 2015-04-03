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
        	toSave = inDb;
            toSave.setResponse(questionResponse.getResponse());
            toSave.setSecretQuestion(questionResponse.getSecretQuestion());
        } else {
            // check to see how many responses are currently stored
            if (userClientSecretQuestionResponsePersistence.findByUserClient(questionResponse.getUserClient(), null)
            		.getNumberOfElements() >= 2){
                throw new InvalidDataAccessApiUsageException("There are already 2 responses in the database");
            } else{
                toSave = questionResponse;
            }
        }
        return userClientSecretQuestionResponsePersistence.saveOrUpdate(toSave);
    }
    
    
    /**
     * List all secret questions
     * @param pageable the pagination specification
     * @return page SecretQuestion
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SecretQuestion> list(Pageable pageable) {
        return secretQuestionPersistence.findAll(pageable);
    }

    /**
     * Reloads the question response 
     * @param questionResponse the question response
     * @return  UserClientSecretQuestionResponse
     */
    @Override
    @Transactional(readOnly = true)
    public UserClientSecretQuestionResponse reload(
            UserClientSecretQuestionResponse questionResponse) {
        if (questionResponse == null || questionResponse.getId() == null) {
            return null;
        }
        return userClientSecretQuestionResponsePersistence.reload(questionResponse);
    }

    /**
     * Find security question by user client
     * @param userClient the user client
     * @param pageable the pagination
     * @return  Page of UserClientSecretQuestionResponse
     */
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
    
    /**
     * save or update user client for user client created or not flag  
     * @param userClient the user client
     * @return  UserClient
     */
    @Override
    @Transactional
    public UserClient saveOrUpdateUserClient(
            UserClient userClient) {
    	UserClient inDb = userClientService.reload(userClient);
        if (inDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        inDb.setSecretQuestionCreated(userClient.isSecretQuestionCreated());
    	return userClientPersistence.saveOrUpdate(inDb);
    }

    /**
     * find user client with reset password token then find secret question for the found user  
     * @param userClient the user client
     * @return  UserClient
     */
	@Override
	@Transactional(readOnly = true)
	public Page<UserClientSecretQuestionResponse> findSecretQuestionToken(
			String resetToken,Pageable pageable) {
		
		if (resetToken != null) {
            UserClient userClient =
                    userClientPersistence.findByResetToken(resetToken);
            return userClientSecretQuestionResponsePersistence
                    .findByUserClient(userClient, pageable);
            
		} 
		else{
			return null;
		}
 
	}

	/**
     * Validate user input security response with database response  
     * @param resetToken the password reset token
     * @param questionResponse list of user input response
     * @return  boolean is input response match
     */
	@Override
	@Transactional(readOnly = true)
	public boolean validateSecurityResponse(
			String resetToken,
			List<UserClientSecretQuestionResponse> questionResponse) {
		if (resetToken != null) {
            UserClient userClient =
                    userClientPersistence.findByResetToken(resetToken);
            Page<UserClientSecretQuestionResponse> dbResponse= userClientSecretQuestionResponsePersistence
                    .findByUserClient(userClient, new PageRequest(0, 10));
            
           return compareSecurityResponse(questionResponse, dbResponse);
        }
     	else{
			return false;
		}
	}
	
	private boolean compareSecurityResponse(List<UserClientSecretQuestionResponse> questionResponse, Page<UserClientSecretQuestionResponse> dbResponse){
		int counter = 0;
		boolean isResponseSame = false;
		for(UserClientSecretQuestionResponse response : questionResponse){
			Long questionId = response.getSecretQuestion().getId();
			for(UserClientSecretQuestionResponse databaseResponse : dbResponse.getContent()){
				if(databaseResponse.getSecretQuestion().getId()==questionId){
					if(response.getResponse().replaceAll("\\s+", "").equalsIgnoreCase(databaseResponse.getResponse().replaceAll("\\s+", ""))){
						counter ++;
				    }
				}
			}
		}
		isResponseSame = (counter == 2) ? true : false;
		return isResponseSame;
	}

}

