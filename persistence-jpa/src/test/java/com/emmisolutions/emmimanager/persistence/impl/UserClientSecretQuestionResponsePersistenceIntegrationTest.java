package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.SecretQuestion;
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
	
	 @Resource
	 UserClientRepository userClientRepository;
	 
	
    @Test
    public void testFind() {
        UserClient user = createUserClient();
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        UserClient user1 = userClientRepository.findOne(user.getId());
        assertThat("the users saved should be the same as the user fetched",
                user, is(user1));
         Page<UserClientSecretQuestionResponse> findByUser = userClientSecretQuestionResponsePersistence
                .findByUserClient(user1, null);
        assertThat("Find pageable question response with client Id", findByUser.hasContent(), is(false));
        
              
    }
    
	/**
     * 
     * Save success
    */
    @Test
    public void save() {
    	Client client = makeNewRandomClient();
        UserClient user = createUserClient();      
        user.setClient(client);
        user.setFirstName("Test");
        user.setLastName("lastName");
        user.setLogin("tete@mail.com");
        user.setEmail("tete@gmail.com");
        user = userClientPersistence.saveOrUpdate(user);
        SecretQuestion secretQuestion = new SecretQuestion();
        secretQuestion.setSecretQuestion("What was the make and model of your first car?");
        SecretQuestion secretQuestionId  = secretQuestionPersistence.save(secretQuestion);

    	UserClientSecretQuestionResponse  questionResponse= new UserClientSecretQuestionResponse();
    	questionResponse.setSecretQuestion(secretQuestionId);
    	questionResponse.setResponse("Response");
    	questionResponse.setUserClient(user);
    	questionResponse = (UserClientSecretQuestionResponse) userClientSecretQuestionResponsePersistence.saveOrUpdate(questionResponse);
    	userClientSecretQuestionResponsePersistence.reload(questionResponse.getId());    	
        assertThat("Client was given an id", questionResponse.getId(), is(notNullValue()));
          
        UserClientSecretQuestionResponse findByClientIdAndQuestionId = userClientSecretQuestionResponsePersistence.reload(questionResponse);
        
        assertThat("Find list question response with client Id", findByClientIdAndQuestionId, is(notNullValue()));
        
        Page<UserClientSecretQuestionResponse> findByUser = userClientSecretQuestionResponsePersistence
                .findByUserClient(user, new PageRequest(0, 10));
        assertThat("Find pageable question response with client Id", findByUser.hasContent(), is(true));
    } 
    
       
    private UserClient createUserClient()
    {
        Client client = makeNewRandomClient();
        
        UserClient user = new UserClient();
        user.setClient(client);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setLogin("flast@mail.com");
        user.setEmail("flast@gmail.com");
        user = userClientPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        UserClient user1 = userClientRepository.findOne(user.getId());
        assertThat("the users saved should be the same as the user fetched",
                user, is(user1));
        
        return user;
    }
    
}
