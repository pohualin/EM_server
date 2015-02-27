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
 * An integration test that goes across a wired persistence layer as well
 */
public class UserClientSecretQuestionResponseServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientSecretQuestionResponseService userClientSecretQuestionResponseService;

    @Resource
    SecretQuestionRepository secretQuestionRepository;
        
    @Test
    public void loadAllSecretQuestions(){
        List<SecretQuestion> list = createSecretQuestions();
        secretQuestionRepository.save(list);
        Page<SecretQuestion> set = userClientSecretQuestionResponseService.list(new PageRequest(0, 10));
        assertThat("SecretQuestion has been created", set.hasContent(), is(true) );
     
    }   

    @Test
    public void testFindEmpty() {
        Client client = makeNewRandomClient();
        UserClient user = new UserClient();
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
    public void invalidSecretQuestion(){
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        List<SecretQuestion> list = createSecretQuestions();
        secretQuestionRepository.save(list);
        UserClientSecretQuestionResponse  questionResponse= new UserClientSecretQuestionResponse();
        questionResponse.setUserClient(userClient);
        questionResponse.setSecretQuestion(list.get(1));
        questionResponse = (UserClientSecretQuestionResponse) userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);
        
     }

    
    /**
     * Reload test
    */
    @Test
    public void testSaveAndReload() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        List<SecretQuestion> list = createSecretQuestions();
        SecretQuestion secretQuestion = secretQuestionRepository.save(list.get(0));
        UserClientSecretQuestionResponse  questionResponse= new UserClientSecretQuestionResponse();
        questionResponse.setSecretQuestion(secretQuestion);
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
        user.setFirstName("SecondName");
        user.setLastName("AnotherName");
        user.setLogin("wee@mail.com");
        user.setEmail("wee@gmail.com");
        user = userClientService.create(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
        
        List<SecretQuestion> list = createSecretQuestions();
        SecretQuestion secretQuestion = secretQuestionRepository.save(list.get(0));
        SecretQuestion secretQuestionToo = secretQuestionRepository.save(list.get(1));
        
        UserClientSecretQuestionResponse  questionResponse= new UserClientSecretQuestionResponse();
        UserClientSecretQuestionResponse  questionResponseToo= new UserClientSecretQuestionResponse();
        questionResponse.setSecretQuestion(secretQuestion);
        questionResponse.setResponse("Toyota");
        questionResponse.setUserClient(user);
        questionResponse = (UserClientSecretQuestionResponse) userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);
        
        questionResponseToo.setSecretQuestion(secretQuestionToo);
        questionResponseToo.setResponse("Sushi");
        questionResponseToo.setUserClient(user);
       
        questionResponseToo = (UserClientSecretQuestionResponse)userClientSecretQuestionResponseService.saveOrUpdate(questionResponseToo);
        userClientSecretQuestionResponseService.reload(questionResponse);   
        
        userClientSecretQuestionResponseService.reload(questionResponseToo);    
        
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
    } 
    
    private List<SecretQuestion> createSecretQuestions(){
        List<SecretQuestion> list = new ArrayList<SecretQuestion>();
        SecretQuestion secretQuestion = new SecretQuestion();
        secretQuestion.setSecretQuestion("What was the make and model of your first car?");
        secretQuestion.setRank(1);
        
        SecretQuestion secretQuestionToo = new SecretQuestion();
        secretQuestionToo.setSecretQuestion("What is your favor food?");
        secretQuestionToo.setRank(2);
           
        list.add(secretQuestion);
        list.add(secretQuestionToo);
        return list;
    }
    
}
