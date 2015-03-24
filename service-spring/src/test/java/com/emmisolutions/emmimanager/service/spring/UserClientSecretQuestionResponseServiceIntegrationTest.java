package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.SecretQuestionPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.repo.SecretQuestionRepository;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;
import com.emmisolutions.emmimanager.service.UserClientService;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for user client secret question reponse service
 */
public class UserClientSecretQuestionResponseServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientService userClientService;
    
    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientSecretQuestionResponseService userClientSecretQuestionResponseService;

    @Test
    public void testFindEmpty() {
        Client client = makeNewRandomClient();
        UserClient user = makeNewRandomUserClient(client);
        user.setClient(client);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setLogin("wen@mail.com");
        user.setEmail("wen@gmail.com");
        user = userClientService.create(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
        
        Page<UserClientSecretQuestionResponse> page2= userClientSecretQuestionResponseService
                .findByUserClient(user, new PageRequest(0, 10));
        assertThat("SecretQuestion has been created", page2.hasContent(), is(false));
        
    }
    
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidClientId(){
        Page<UserClientSecretQuestionResponse> page1= userClientSecretQuestionResponseService
                .findByUserClient(null, null);
        assertThat("SecretQuestion has been created", page1.hasContent(), is(false ));

    }
    
       
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testSaveAndUpdateInvalid() {
        UserClient user = new UserClient();
        userClientSecretQuestionResponseService.saveOrUpdateUserClient(user);
    }

    
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidSecretQuestion(){
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        Page<SecretQuestion> list = userClientSecretQuestionResponseService.list(new PageRequest(0, 10));
        UserClientSecretQuestionResponse  questionResponse= new UserClientSecretQuestionResponse();
        questionResponse.setUserClient(userClient);
        questionResponse.setSecretQuestion(list.getContent().get(1));
        questionResponse = (UserClientSecretQuestionResponse) userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);
        
     }
    
    /**
     * Reload test
    */
    @Test
    public void saveOrUpdateUserClient(){
    	Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        userClient = userClientService.reload(userClient);
        userClient.setSecretQuestionCreated(true);
        userClient = userClientPersistence.saveOrUpdate(userClient);
        assertThat("user client secret question created is true", userClient.isSecretQuestionCreated(), is(true));
            
     }
    
    /**
     * Null Test
    */
    @Test
    public void testQuestionResponseIsNull() {
    	UserClientSecretQuestionResponse questionResponse = userClientSecretQuestionResponseService.reload(null);
        assertThat(questionResponse, is(nullValue()));
    }
    
      
    /**
     * Reload test
    */
    @Test
    public void testSaveAndReload() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        Page<SecretQuestion> set = userClientSecretQuestionResponseService.list(new PageRequest(0, 10));
        UserClientSecretQuestionResponse  questionResponse= new UserClientSecretQuestionResponse();
        questionResponse.setSecretQuestion(set.getContent().get(1));
        questionResponse.setResponse("Response");
        questionResponse.setUserClient(userClient);
        questionResponse = (UserClientSecretQuestionResponse) userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);
        assertThat("SecretQuestion has been created", questionResponse, is(questionResponse));
        userClientSecretQuestionResponseService.reload(questionResponse);    
        assertThat("Should return null", userClientSecretQuestionResponseService.reload(questionResponse ), is(notNullValue()));
       
    }
    
    /**
     * 
     * Save success
     */
    @Test
    public void CreateAndFind() {
        
        Client client = makeNewRandomClient();
        
        UserClient user = new UserClient();
        user.setClient(client);
        user.setSecretQuestionCreated(false);
        user.setFirstName("SecondName");
        user.setLastName("AnotherName");
        user.setLogin("wee@mail.com");
        user.setEmail("wee@gmail.com");
        user = userClientService.create(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
        
        Page<SecretQuestion> list = userClientSecretQuestionResponseService.list(new PageRequest(0, 10));
        UserClientSecretQuestionResponse  questionResponse= new UserClientSecretQuestionResponse();
        UserClientSecretQuestionResponse  questionResponseToo= new UserClientSecretQuestionResponse();
        questionResponse.setSecretQuestion(list.getContent().get(1));
        questionResponse.setResponse("Toyota");
        questionResponse.setUserClient(user);
        questionResponse = (UserClientSecretQuestionResponse) userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);
        
        questionResponseToo.setSecretQuestion(list.getContent().get(2));
        questionResponseToo.setResponse("Sushi");
        questionResponseToo.setUserClient(user);
       
        questionResponseToo = (UserClientSecretQuestionResponse)userClientSecretQuestionResponseService.saveOrUpdate(questionResponseToo);
        user = userClientService.reload(user);
        
        user.setSecretQuestionCreated(true);
        
        user = userClientPersistence.saveOrUpdate(user);
                             
        Page<UserClientSecretQuestionResponse> page= userClientSecretQuestionResponseService
                .findByUserClient(user, new PageRequest(0, 10));
        
        assertThat("secret question response has been created", page.hasContent(),
                is(true));
        
        assertThat("secret question response has been created", page.getNumberOfElements(),
                is(2));
        
        assertThat("Client was given an id", questionResponse.getId(), is(notNullValue()));
        assertThat("system is the created by", questionResponse.getCreatedBy(), is("system"));
        assertThat("system is the response", questionResponse.getResponse(), is("Toyota"));
        assertThat("Should return null", userClientSecretQuestionResponseService.reload(questionResponseToo), is(notNullValue()));
        assertThat("user client secret question created is true", user.isSecretQuestionCreated(), is(true));
    } 
     
}
