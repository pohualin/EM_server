package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientSecretQuestionResponsePersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;
import com.emmisolutions.emmimanager.service.UserClientService;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for user client secret question response service
 */
public class UserClientSecretQuestionResponseServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientPasswordService userClientPasswordService;

    @Resource
    UserClientSecretQuestionResponsePersistence userClientSecretQuestionResponsePersistence;

    @Resource
    UserClientSecretQuestionResponseService userClientSecretQuestionResponseService;

    /**
     * Empty secret questions should be created
     */
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

        Page<UserClientSecretQuestionResponse> page2 = userClientSecretQuestionResponseService
                .findByUserClient(user, new PageRequest(0, 10));
        assertThat("SecretQuestion has been created", page2.hasContent(), is(false));

    }

    /**
     * Basic bad create test
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidClientId() {
        Page<UserClientSecretQuestionResponse> page1 = userClientSecretQuestionResponseService
                .findByUserClient(null, null);
        assertThat("SecretQuestion has been created", page1.hasContent(), is(false));
    }

    /**
     * Invalid user client
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveAndUpdateInvalid() {
        UserClient user = new UserClient();
        userClientSecretQuestionResponseService.saveOrUpdateUserClient(user);
    }

    /**
     * Invalid secret question
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidSecretQuestion() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        Page<SecretQuestion> list = userClientSecretQuestionResponseService.list(new PageRequest(0, 10));
        UserClientSecretQuestionResponse questionResponse = new UserClientSecretQuestionResponse();
        questionResponse.setUserClient(userClient);
        questionResponse.setSecretQuestion(list.getContent().get(1));
        userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);
    }


    /**
     * Test security question response is null
     */
    @Test
    public void questionResponseIsNull() {
        UserClientSecretQuestionResponse questionResponse = userClientSecretQuestionResponseService.reload(null);
        assertThat(questionResponse, is(nullValue()));
    }


    /**
     * Reload test
     */
    @Test
    public void saveAndReload() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        Page<SecretQuestion> set = userClientSecretQuestionResponseService.list(new PageRequest(0, 10));
        UserClientSecretQuestionResponse questionResponse = new UserClientSecretQuestionResponse();
        questionResponse.setSecretQuestion(set.getContent().get(1));
        questionResponse.setResponse("Response");
        questionResponse.setUserClient(userClient);
        questionResponse = userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);
        assertThat("SecretQuestion has been created", questionResponse, is(questionResponse));
        userClientSecretQuestionResponseService.reload(questionResponse);
        assertThat("Should return null", userClientSecretQuestionResponseService.reload(questionResponse), is(notNullValue()));

    }


    /**
     * Save or update if security question created or not test
     */
    @Test
    public void saveOrUpdate() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClient inDb = userClientService.reload(userClient);
        inDb.setSecretQuestionCreated(true);
        UserClient updateUser = userClientSecretQuestionResponseService.saveOrUpdateUserClient(inDb);
        assertThat("SecretQuestion has been created", updateUser.isSecretQuestionCreated(), is(true));

    }

    /**
     * Makes sure the finder doesn't puke when null is passed in
     */
    @Test
    public void nullTokenReturnsNull() {
        assertThat("null token returns null",
                userClientSecretQuestionResponseService.findSecretQuestionToken(null, null),
                is(nullValue()));
    }

    /**
     * Make sure that the isSecurityQuestionsNotRequiredForReset flag is respected
     */
    @Test
    public void makeSureUsersCanBypassQuestions() {
        String resetToken = "B473E2147988F8D67B62457B115A6EB7B8F2C555";
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPasswordResetToken(resetToken);
        userClient.setPasswordResetExpirationDateTime(LocalDateTime.now().plusDays(1));
        userClient.setSecurityQuestionsNotRequiredForReset(true);
        userClientPersistence.saveOrUpdate(userClient);
        assertThat("The response should validate to true because the security questions are not required",
                userClientSecretQuestionResponseService.validateSecurityResponse(resetToken, null), is(true));
    }

    /**
     * Make sure that if we try to validate a null response it is false
     */
    @Test
    public void blankResponsesShouldValidateToFalse() {
        String resetToken = "B473E2147988F8D67B62457B115A6EB7B8F2CAAA";
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPasswordResetToken(resetToken);
        userClient.setPasswordResetExpirationDateTime(LocalDateTime.now().plusDays(1));
        userClient.setSecurityQuestionsNotRequiredForReset(false);
        userClientPersistence.saveOrUpdate(userClient);
        assertThat("The response should validate to false because we passed no response in",
                userClientSecretQuestionResponseService.validateSecurityResponse(resetToken, null), is(false));
    }

    /**
     * Make sure that if the token is not expired we dont throw exception
     */
    @Test
    public void dontThrowExceptionIfTokenIsNotExpired() {
        String resetToken = "B473E2147988F8D67B62457B115A6EB7B8F2CAAZ";
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPasswordResetToken(resetToken);
        userClient.setPasswordResetExpirationDateTime(LocalDateTime.now().plusDays(1));
        userClient.setSecurityQuestionsNotRequiredForReset(true);
        userClientPersistence.saveOrUpdate(userClient);
        assertThat("The response should validate to true because token is not expired",
                userClientSecretQuestionResponseService.validateSecurityResponse(resetToken, null), is(true));
    }

    /**
     * return true if expiration is null
     */
    @Test
    public void testExpirationNull() {
        String resetToken = "B473E2147988F8D67B62457B115A6EB7B8F2CAAY";
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPasswordResetToken(resetToken);
        userClient.setPasswordResetExpirationDateTime(null);
        userClient.setSecurityQuestionsNotRequiredForReset(true);
        userClientPersistence.saveOrUpdate(userClient);
        assertThat("The response should validate to true because there is no expiration date",
                userClientSecretQuestionResponseService.validateSecurityResponse(resetToken, null), is(true));
    }

    /**
     * Make sure that if the token is expired we throw exception
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void throwExceptionIfTokenIsExpired() {
        String resetToken = "B473E2147988F8D67B62457B115A6EB7B8F2CAAX";
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPasswordResetToken(resetToken);
        userClient.setPasswordResetExpirationDateTime(LocalDateTime.now().minusDays(1));
        userClientPersistence.saveOrUpdate(userClient);
        userClientSecretQuestionResponseService.validateSecurityResponse(resetToken, null);
    }

    /**
     * Not passing a reset token to validate should cause an
     * exception
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void validateNotFoundToken() {
        userClientSecretQuestionResponseService.validateSecurityResponse(null, null);
    }

    /**
     * Test comparing security response for users
     */
    @Test
    public void compareSecurityQuestion() {
        String resetToken = "B473E2147988F8D67B62457B115A6EB7B8F2C277";
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        userClient.setPasswordResetToken(resetToken);
        userClient.setPasswordResetExpirationDateTime(LocalDateTime.now().plusDays(1));
        UserClient userAfterSave = userClientPersistence.saveOrUpdate(userClient);

        assertThat("secret question created true", userAfterSave.isSecretQuestionCreated(), is(false));

        Page<SecretQuestion> set = userClientSecretQuestionResponseService.list(new PageRequest(0, 10));
        UserClientSecretQuestionResponse questionResponse = new UserClientSecretQuestionResponse();
        questionResponse.setSecretQuestion(set.getContent().get(1));
        questionResponse.setResponse("Response1");
        questionResponse.setUserClient(userAfterSave);
        questionResponse = userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);

        UserClientSecretQuestionResponse questionResponse2 = new UserClientSecretQuestionResponse();
        questionResponse2.setSecretQuestion(set.getContent().get(2));
        questionResponse2.setResponse("Response2");
        questionResponse2.setUserClient(userAfterSave);
        userClientSecretQuestionResponseService.saveOrUpdate(questionResponse2);

        userAfterSave.setSecretQuestionCreated(true);
        UserClient userSaveAgain = userClientPersistence.saveOrUpdate(userAfterSave);
        assertThat("secret question created true", userSaveAgain.isSecretQuestionCreated(), is(true));

        List<UserClientSecretQuestionResponse> list = new ArrayList<>();
        UserClientSecretQuestionResponse questionResponse3 = new UserClientSecretQuestionResponse();
        questionResponse3.setSecretQuestion(set.getContent().get(1));
        questionResponse3.setResponse("Response1");
        UserClientSecretQuestionResponse questionResponse4 = new UserClientSecretQuestionResponse();
        questionResponse4.setSecretQuestion(set.getContent().get(2));
        questionResponse4.setResponse("Response2");
        list.add(questionResponse3);
        list.add(questionResponse4);


        assertThat("user client is not null", resetToken, is(notNullValue()));
        assertThat("SecretQuestion has been created",
                userClientSecretQuestionResponseService.validateSecurityResponse(resetToken,
                        questionResponse), is(true));
        UserClient userClientByToken =
                userClientPersistence.findByResetToken(resetToken);
        Page<UserClientSecretQuestionResponse> dbResponse = userClientSecretQuestionResponsePersistence
                .findByUserClient(userClientByToken, new PageRequest(0, 10));

        boolean isSame = compareSecurityResponse(list, dbResponse);
        assertThat("SecretQuestion has been created", isSame, is(true));

        List<UserClientSecretQuestionResponse> list2 = new ArrayList<>();
        UserClientSecretQuestionResponse questionResponse5 = new UserClientSecretQuestionResponse();
        questionResponse5.setSecretQuestion(set.getContent().get(1));
        questionResponse5.setResponse("ResponseNo");
        UserClientSecretQuestionResponse questionResponse6 = new UserClientSecretQuestionResponse();
        questionResponse6.setSecretQuestion(set.getContent().get(2));
        questionResponse6.setResponse("ResponseYes");
        list2.add(questionResponse5);
        list2.add(questionResponse6);

        boolean isSameAgain = compareSecurityResponse(list2, dbResponse);
        assertThat("SecretQuestion has been created", isSameAgain, is(false));

        assertThat("user client is not null", userClientByToken, is(notNullValue()));
        assertThat("user client doesn't reset password", userClientByToken.getPasswordResetToken(), is(resetToken));
        assertThat("Should return null", userClientSecretQuestionResponseService.reload(questionResponse), is(notNullValue()));

    }

    /**
     * Happy path for finding security question from a reset token
     */
    @Test
    public void findSecurityQuestionWithToken() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        userClient = userClientPasswordService.addResetTokenTo(userClient);
        userClient = userClientPersistence.findByResetToken(userClient.getPasswordResetToken());

        Page<SecretQuestion> set = userClientSecretQuestionResponseService.list(new PageRequest(0, 10));
        UserClientSecretQuestionResponse questionResponse = new UserClientSecretQuestionResponse();
        questionResponse.setSecretQuestion(set.getContent().get(1));
        questionResponse.setResponse("Response");
        questionResponse.setUserClient(userClient);
        questionResponse = userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);

        Page<UserClientSecretQuestionResponse> page = userClientSecretQuestionResponseService
                .findSecretQuestionToken(userClient.getPasswordResetToken(), new PageRequest(0, 10));

        assertThat(page.getContent().get(0).getSecretQuestion(), is(notNullValue()));


        assertThat("secret question response has been created", page.hasContent(),
                is(true));

        assertThat("secret question response has been created", page.getNumberOfElements(),
                is(1));

        assertThat("SecretQuestion has been created", questionResponse, is(questionResponse));
        userClientSecretQuestionResponseService.reload(questionResponse);
        assertThat("Should return null", userClientSecretQuestionResponseService.reload(questionResponse), is(notNullValue()));


    }

    /**
     * Save success
     */
    @Test
    public void createAndFind() {

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
        UserClientSecretQuestionResponse questionResponse = new UserClientSecretQuestionResponse();
        UserClientSecretQuestionResponse questionResponseToo = new UserClientSecretQuestionResponse();
        questionResponse.setSecretQuestion(list.getContent().get(1));
        questionResponse.setResponse("Toyota");
        questionResponse.setUserClient(user);
        questionResponse = userClientSecretQuestionResponseService.saveOrUpdate(questionResponse);

        questionResponseToo.setSecretQuestion(list.getContent().get(2));
        questionResponseToo.setResponse("Sushi");
        questionResponseToo.setUserClient(user);

        questionResponseToo = userClientSecretQuestionResponseService.saveOrUpdate(questionResponseToo);
        UserClient userReload = userClientService.reload(user);

        userReload.setSecretQuestionCreated(user.isSecretQuestionCreated());

        UserClient userAfterSave = userClientPersistence.saveOrUpdate(userReload);
        assertThat("secret question created true", userAfterSave.isSecretQuestionCreated(), is(false));


        Page<UserClientSecretQuestionResponse> page = userClientSecretQuestionResponseService
                .findByUserClient(user, new PageRequest(0, 10));

        assertThat("secret question response has been created", page.hasContent(),
                is(true));

        assertThat("secret question response has been created", page.getNumberOfElements(),
                is(2));

        assertThat("Client was given an id", questionResponse.getId(), is(notNullValue()));
        assertThat("system is the created by", questionResponse.getCreatedBy(), is("system"));
        assertThat("system is the response", questionResponse.getResponse(), is("Toyota"));
        assertThat("Should return null", userClientSecretQuestionResponseService.reload(questionResponseToo), is(notNullValue()));
        assertThat("user client secret question created is true", user.isSecretQuestionCreated(), is(false));
    }

    private boolean compareSecurityResponse(List<UserClientSecretQuestionResponse> questionResponse, Page<UserClientSecretQuestionResponse> dbResponse) {
        int counter = 0;
        for (UserClientSecretQuestionResponse response : questionResponse) {
            for (UserClientSecretQuestionResponse databaseResponse : dbResponse) {
                if (databaseResponse.getSecretQuestion().equals(response.getSecretQuestion())) {
                    if (response.getResponse().replaceAll("\\s+", "")
                            .equalsIgnoreCase(databaseResponse.getResponse().replaceAll("\\s+", ""))) {
                        counter++;
                    }
                }
            }
        }
        return counter == 2;
    }


}
