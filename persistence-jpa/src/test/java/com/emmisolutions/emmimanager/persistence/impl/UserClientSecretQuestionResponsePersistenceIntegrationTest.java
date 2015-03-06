package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientSecretQuestionResponsePersistence;
import com.emmisolutions.emmimanager.persistence.repo.SecretQuestionRepository;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    
    /**
     * Reloading a null should return a null
     */
    @Test
    public void reloadNull(){
    	UserClientSecretQuestionResponse userClientNull = userClientSecretQuestionResponsePersistence.reload(null);
    	assertThat("reload of null returns null", userClientNull, is(nullValue()));
    }
    
    /**
     * Loading via null page request branch
     */
    @Test
    public void loadNullPage() {
    	Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client); 
        assertThat("Reference Roles are loaded", userClientSecretQuestionResponsePersistence.findByUserClient(userClient, null).getTotalElements(), is(0l));
    }
    
    /**
     * Call the find by user client
     */
    public void testFindByUserClient(){
    	Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client); 
        Page<SecretQuestion> set = secretQuestionPersistence.findAll(new PageRequest(0, 10));
        
        UserClientSecretQuestionResponse  questionResponse= new UserClientSecretQuestionResponse();
    	questionResponse.setSecretQuestion(set.getContent().get(1));
    	questionResponse.setResponse("Response");
    	questionResponse.setUserClient(userClient);
    	
    	UserClientSecretQuestionResponse  questionResponse2= new UserClientSecretQuestionResponse();
    	questionResponse2.setSecretQuestion(set.getContent().get(2));
    	questionResponse2.setResponse("Response");
    	questionResponse2.setUserClient(userClient);
    	
    	questionResponse = (UserClientSecretQuestionResponse) userClientSecretQuestionResponsePersistence.saveOrUpdate(questionResponse);
    	questionResponse2 = (UserClientSecretQuestionResponse) userClientSecretQuestionResponsePersistence.saveOrUpdate(questionResponse2);
    	
    	Page<UserClientSecretQuestionResponse> findByUser = userClientSecretQuestionResponsePersistence.findByUserClient(userClient, new PageRequest(0, 10));
        assertThat("should return a page of UserClientUserClientTeamRole",
        		findByUser.hasContent(), is(true));
        assertThat("page is null ", userClientSecretQuestionResponsePersistence.reload(null), is(nullValue()));
        
        
        assertThat("Find pageable question response with user client", findByUser, hasItem(questionResponse)); 
        assertThat("Find pageable question response with user client", findByUser, hasItem(questionResponse2)); 
        
    }
  
    
}
