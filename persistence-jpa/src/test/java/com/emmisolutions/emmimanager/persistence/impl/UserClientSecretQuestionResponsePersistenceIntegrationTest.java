package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientSecretQuestionResponsePersistence;
import com.emmisolutions.emmimanager.persistence.repo.SecretQuestionRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserClientRepository;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * Integration test for UserClientSecretQuestionResponsePersistence
 */
public class UserClientSecretQuestionResponsePersistenceIntegrationTest extends
				BaseIntegrationTest {
	
	@Resource
	UserClientSecretQuestionResponsePersistence userClientSecretQuestionResponsePersistence;
	
	@Resource
    UserClientPersistence userClientPersistence;
	
	@Resource
    SecretQuestionRepository secretQuestionPersistence;
	
	/**
	 * Test save and find
	 */
    @Test
    public void saveAndFind() {
    	Client client = makeNewRandomClient();
        UserClient user = makeNewRandomUserClient(client);   
        
        Page<SecretQuestion> set = secretQuestionPersistence.findAll(new PageRequest(0, 10));

    	UserClientSecretQuestionResponse  questionResponse= new UserClientSecretQuestionResponse();
    	questionResponse.setSecretQuestion(set.getContent().get(1));
    	questionResponse.setResponse("Response");
    	questionResponse.setUserClient(user);
    	questionResponse = (UserClientSecretQuestionResponse) userClientSecretQuestionResponsePersistence.saveOrUpdate(questionResponse);
    	userClientSecretQuestionResponsePersistence.reload(questionResponse);    	
        assertThat("Client was given an id", questionResponse.getId(), is(notNullValue()));
          
        UserClientSecretQuestionResponse findByClientIdAndQuestionId = userClientSecretQuestionResponsePersistence.reload(questionResponse);
        
        assertThat("Find one question response with client Id", findByClientIdAndQuestionId, is(notNullValue()));
        
        Page<UserClientSecretQuestionResponse> findByUser = userClientSecretQuestionResponsePersistence
                .findByUserClient(user, new PageRequest(0, 10));
        
        assertThat("Find pageable question response with user client", findByUser, hasItem(questionResponse));       
    } 
  
    
}
