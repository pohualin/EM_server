package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordHistoryService;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService.UserClientPasswordValidationError;

/**
 * Integration test for the UserClientValidationService
 */
public class UserClientPasswordValidationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientPasswordService userClientPasswordService;

    @Resource
    UserClientPasswordHistoryService userClientPasswordHistoryService;

    @Resource
    UserClientPasswordValidationService userClientPasswordValidationService;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    @Test
    public void validateRequest() {
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPassword("aPassword");
        userClientPasswordService.updatePassword(userClient, true);
        try {
            userClientPasswordValidationService.validateRequest(null);
            fail("ChangePasswordRequest can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        ChangePasswordRequest request = new ChangePasswordRequest();
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("Login can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setLogin(userClient.getLogin());
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("Old password can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setExistingPassword("badPassword");
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("New password can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        UserClientPasswordValidationError badPatternError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.POLICY);
        request.setNewPassword("badPassword");
        assertThat("Existing password does not match",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(badPatternError));

        request.setExistingPassword("aPassword");
        assertThat("Existing password does not match",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(badPatternError));

        request.setNewPassword("abcABC123");
        assertThat("Existing password does not match",
                userClientPasswordValidationService.validateRequest(request)
                        .size(), is(0));
    }

    @Test
    public void validatePasswordPattern() {
        UserClient userClient = makeNewRandomUserClient(null);

        try {
            userClientPasswordValidationService.validatePasswordPattern(null);
            fail("UserClient can not be null");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        UserClient toValidate = new UserClient();
        try {
            userClientPasswordValidationService
                    .validatePasswordPattern(toValidate);
            fail("UserClient password can not be blank.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        toValidate.setLogin("notThere");
        try {
            userClientPasswordValidationService
                    .validatePasswordPattern(toValidate);
            fail("Method only support existing UserClient.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        toValidate.setLogin(userClient.getLogin());
        toValidate.setPassword("abcABC");
        assertThat("Should not match", userClientPasswordValidationService
                .validatePasswordPattern(toValidate).getReason(),
                is(UserClientPasswordValidationService.Reason.POLICY));

        toValidate.setPassword("aaaabcABC");
        assertThat("Should not match", userClientPasswordValidationService
                .validatePasswordPattern(toValidate).getReason(),
                is(UserClientPasswordValidationService.Reason.POLICY));

        toValidate.setPassword("abcABC123");
        assertThat("Should match",
                userClientPasswordValidationService
                        .validatePasswordPattern(toValidate), is(nullValue()));

        toValidate.setPassword("abcABC123[]!");
        assertThat("Should match",
                userClientPasswordValidationService
                        .validatePasswordPattern(toValidate), is(nullValue()));
    }

    @Test
    public void checkPasswordHistory() {
        UserClient userClient = makeNewRandomUserClient(null);
        Client client = userClient.getClient();
        userClient.setPassword("password1");
        userClient = userClientPersistence
                .saveOrUpdate(userClientPasswordService
                        .encodePassword(userClient));

        UserClientPasswordHistory historyA = new UserClientPasswordHistory();
        historyA.setUserClient(userClient);
        historyA.setPasswordSavedTime(LocalDateTime.now().minusDays(1));
        ;
        historyA.setPassword(userClient.getPassword());
        historyA.setSalt(userClient.getSalt());
        historyA = userClientPasswordHistoryService.save(historyA);

        userClient.setPassword("password2");
        userClient = userClientPersistence
                .saveOrUpdate(userClientPasswordService
                        .encodePassword(userClient));
        UserClientPasswordHistory historyB = new UserClientPasswordHistory();
        historyB.setUserClient(userClient);
        historyB.setPasswordSavedTime(LocalDateTime.now().minusDays(2));
        ;
        historyB.setPassword(userClient.getPassword());
        historyB.setSalt(userClient.getSalt());
        historyB = userClientPasswordHistoryService.save(historyB);

        UserClient toValidate = new UserClient();
        toValidate.setLogin(userClient.getLogin());
        toValidate.setPassword("password3");
        assertThat("Password does not repeat",
                userClientPasswordValidationService
                        .checkPasswordHistory(toValidate), is(nullValue()));

        toValidate.setPassword("password1");
        assertThat("Password repeats", userClientPasswordValidationService
                .checkPasswordHistory(toValidate).getReason(),
                is(UserClientPasswordValidationService.Reason.HISTORY));

        toValidate.setPassword("password2");
        assertThat("Password repeats", userClientPasswordValidationService
                .checkPasswordHistory(toValidate).getReason(),
                is(UserClientPasswordValidationService.Reason.HISTORY));

        ClientPasswordConfiguration configuration = makeNewRandomClientPasswordConfiguration(client);
        configuration.setPasswordRepetitions(1);
        clientPasswordConfigurationService.save(configuration);

        toValidate.setPassword("password1");
        assertThat("Password repeats", userClientPasswordValidationService
                .checkPasswordHistory(toValidate).getReason(),
                is(UserClientPasswordValidationService.Reason.HISTORY));

        toValidate.setPassword("password2");
        assertThat("Password does not repeat",
                userClientPasswordValidationService
                        .checkPasswordHistory(toValidate), is(nullValue()));

    }

    @Test
    public void negativeCheckPasswordHistory() {
        try {
            userClientPasswordValidationService.checkPasswordHistory(null);
            fail("UserClient can not be null");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        try {
            userClientPasswordValidationService
                    .checkPasswordHistory(new UserClient());
            fail("Password can not be null");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        UserClient userClient = new UserClient();
        userClient.setPassword("password");
        try {
            userClientPasswordValidationService
                    .checkPasswordHistory(userClient);
            fail("This method is only to be used with existing UserClient");
        } catch (InvalidDataAccessApiUsageException e) {
        }
    }
}
