package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordHistoryService;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService.UserClientPasswordValidationError;
import com.emmisolutions.emmimanager.service.UserClientService;

/**
 * Integration test for the UserClientValidationService
 */
public class UserClientPasswordValidationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientPasswordService userClientPasswordService;

    @Resource
    UserClientPasswordHistoryService userClientPasswordHistoryService;

    @Resource
    UserClientPasswordValidationService userClientPasswordValidationService;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    @Test
    public void validateChangePasswordRequest() {
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPassword("currentPassword1");
        userClient = userClientPasswordService.updatePassword(userClient, true);

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

        request.setExistingPassword("currentPassword");
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("New password can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setLogin("badLogin");
        request.setNewPassword("bad");
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("Can only be called by existed UserClient.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setLogin(userClient.getLogin());
        UserClientPasswordValidationError policyError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.POLICY);
        request.setNewPassword("bad");
        assertThat("New password does not pass policy check",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(policyError));

        UserClientPasswordValidationError historyError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.HISTORY);
        request.setNewPassword("currentPassword1");
        assertThat("New password repeats",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(historyError));

        request.setNewPassword("currentPassword2");
        assertThat("Good password", userClientPasswordValidationService
                .validateRequest(request).size(), is(0));

        UserClientPasswordValidationError repeatError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.DAYS_BETWEEN);
        userClient = userClientPasswordService
                .updatePasswordExpirationTime(userClient);
        assertThat("Repeated password",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(repeatError));
    }

    @Test
    public void validatePasswordPattern() {
        UserClient userClient = makeNewRandomUserClient(null);
        ClientPasswordConfiguration configuration = makeNewRandomClientPasswordConfiguration(userClient
                .getClient());

        assertThat("Should not match",
                userClientPasswordValidationService.validatePasswordPattern(
                        null, null), is(false));

        assertThat("Should not match",
                userClientPasswordValidationService.validatePasswordPattern(
                        configuration, null), is(false));

        assertThat("Should not match",
                userClientPasswordValidationService.validatePasswordPattern(
                        configuration, "abcABC"), is(false));

        assertThat("Should not match",
                userClientPasswordValidationService.validatePasswordPattern(
                        configuration, "aaaabcABC"), is(false));

        assertThat("Should match",
                userClientPasswordValidationService.validatePasswordPattern(
                        configuration, "abcABC123"), is(true));

        assertThat("Should match",
                userClientPasswordValidationService.validatePasswordPattern(
                        configuration, "abcABC123[]!"), is(true));

        configuration.setSpecialChars(true);
        configuration = clientPasswordConfigurationService.save(configuration);

        assertThat("Should not match",
                userClientPasswordValidationService.validatePasswordPattern(
                        configuration, "abcABC123"), is(false));

        assertThat("Should match",
                userClientPasswordValidationService.validatePasswordPattern(
                        configuration, "abcABC123[]!"), is(true));
    }

    @Test
    public void isPasswordNotRepeatsHistory() {
        UserClient userClient = makeNewRandomUserClient(null);
        Client client = userClient.getClient();
        ClientPasswordConfiguration configuration = makeNewRandomClientPasswordConfiguration(client);

        userClient.setPassword("password1");
        userClient = userClientPersistence
                .saveOrUpdate(userClientPasswordService
                        .encodePassword(userClient));

        assertThat("Password does not repeat",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(configuration, userClient,
                                "password2"), is(true));

        assertThat("Password repeats",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(configuration, userClient,
                                "password1"), is(false));

        UserClientPasswordHistory historyA = new UserClientPasswordHistory();
        historyA.setUserClient(userClient);
        historyA.setPassword(userClient.getPassword());
        historyA.setSalt(userClient.getSalt());
        historyA = userClientPasswordHistoryService.save(historyA);

        userClient.setPassword("currentPassword");
        userClient = userClientPersistence
                .saveOrUpdate(userClientPasswordService
                        .encodePassword(userClient));

        UserClientPasswordHistory historyB = new UserClientPasswordHistory();
        historyB.setUserClient(userClient);
        historyB.setPassword(userClient.getPassword());
        historyB.setSalt(userClient.getSalt());
        historyB = userClientPasswordHistoryService.save(historyB);

        assertThat("Invalid request",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(null, null, null),
                is(false));

        assertThat("Invalid request",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(
                                new ClientPasswordConfiguration(), null, null),
                is(false));

        assertThat("Invalid request",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(
                                new ClientPasswordConfiguration(),
                                new UserClient(), null), is(false));

        assertThat("Password does not repeat",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(configuration, userClient,
                                "password2"), is(true));

        assertThat("Password repeats",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(configuration, userClient,
                                "password1"), is(false));

        assertThat("Password repeats",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(configuration, userClient,
                                "currentPassword"), is(false));

        configuration.setPasswordRepetitions(1);
        clientPasswordConfigurationService.save(configuration);

        assertThat("Password repeats",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(configuration, userClient,
                                "currentPassword"), is(false));

        assertThat("Password does not repeat",
                userClientPasswordValidationService
                        .isPasswordNotRepeatsHistory(configuration, userClient,
                                "password1"), is(true));

    }

    @Test
    public void isEligibleForPasswordChange() {
        UserClient userClient = makeNewRandomUserClient(null);
        Client client = userClient.getClient();
        ClientPasswordConfiguration configuration = makeNewRandomClientPasswordConfiguration(client);

        assertThat("Not eligible for password change",
                userClientPasswordValidationService
                        .isEligibleForPasswordChange(null, null), is(false));

        assertThat("Not eligible for password change",
                userClientPasswordValidationService
                        .isEligibleForPasswordChange(configuration, null),
                is(false));

        assertThat(
                "Eligible for password change",
                userClientPasswordValidationService
                        .isEligibleForPasswordChange(configuration, userClient),
                is(true));

        userClient.setPassword("password");
        userClient = userClientPersistence
                .saveOrUpdate(userClientPasswordService
                        .encodePassword(userClient));
        userClient = userClientPasswordService
                .updatePasswordExpirationTime(userClient);

        assertThat(
                "Not eligible for password change",
                userClientPasswordValidationService
                        .isEligibleForPasswordChange(configuration, userClient),
                is(false));
    }

    @Test
    public void testValidateExpiredPassword() {

        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPassword("currentPassword1");
        userClient = userClientPasswordService.updatePassword(userClient, true);

        ExpiredPasswordChangeRequest request = new ExpiredPasswordChangeRequest();
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

        request.setExistingPassword("currentPassword");
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("New password can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setLogin("badLogin");
        request.setNewPassword("bad");
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("Can only be called by existed UserClient.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setLogin(userClient.getLogin());
        UserClientPasswordValidationError policyError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.POLICY);
        request.setNewPassword("bad");
        assertThat("New password does not pass policy check",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(policyError));

        UserClientPasswordValidationError historyError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.HISTORY);
        request.setNewPassword("currentPassword1");
        assertThat("New password repeats",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(historyError));

        request.setNewPassword("currentPassword2");
        assertThat("Good password", userClientPasswordValidationService
                .validateRequest(request).size(), is(0));

        UserClientPasswordValidationError repeatError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.DAYS_BETWEEN);
        userClient = userClientPasswordService
                .updatePasswordExpirationTime(userClient);
        assertThat("Repeated password",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(repeatError));
    }

    @Test
    public void testValidateResetPassword() {

        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setEmail("apple@abc.com");
        userClientService.update(userClient);
        userClient.setPassword("currentPassword1");
        userClient = userClientPasswordService.updatePassword(userClient, true);
        userClient = userClientPasswordService.forgotPassword("apple@abc.com");
        String token = userClient.getPasswordResetToken();

        ResetPasswordRequest request = new ResetPasswordRequest();

        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("Token can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setResetToken(token);
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("New password can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setResetToken("badToken");
        request.setNewPassword("bad");
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("Can only be called by existed reset token.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setResetToken(token);
        UserClientPasswordValidationError policyError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.POLICY);
        request.setNewPassword("bad");
        assertThat("New password does not pass policy check",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(policyError));

        UserClientPasswordValidationError historyError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.HISTORY);
        request.setNewPassword("currentPassword1");
        assertThat("New password repeats",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(historyError));

        request.setNewPassword("currentPassword2");
        assertThat("Good password", userClientPasswordValidationService
                .validateRequest(request).size(), is(0));

        UserClientPasswordValidationError repeatError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.DAYS_BETWEEN);
        userClient = userClientPasswordService
                .updatePasswordExpirationTime(userClient);
        assertThat("Repeated password",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(repeatError));
    }

    @Test
    public void testValidateActivatePassword() {

        UserClient userClient = makeNewRandomUserClient(null);
        userClient = userClientService.addActivationKey(new UserClient(
                userClient.getId()));
        userClient.setPassword("currentPassword1");
        userClient = userClientPasswordService.updatePassword(userClient, true);

        ActivationRequest request = new ActivationRequest();
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("Token can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setActivationToken(userClient.getActivationKey());
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("New password can not be null.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setActivationToken("badToken");
        request.setNewPassword("bad");
        try {
            userClientPasswordValidationService.validateRequest(request);
            fail("Can only be called by existed activation token.");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        request.setActivationToken(userClient.getActivationKey());
        UserClientPasswordValidationError policyError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.POLICY);
        request.setNewPassword("bad");
        assertThat("New password does not pass policy check",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(policyError));

        UserClientPasswordValidationError historyError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.HISTORY);
        request.setNewPassword("currentPassword1");
        assertThat("New password repeats",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(historyError));

        request.setNewPassword("currentPassword2");
        assertThat("Good password", userClientPasswordValidationService
                .validateRequest(request).size(), is(0));

        UserClientPasswordValidationError repeatError = new UserClientPasswordValidationError(
                UserClientPasswordValidationService.Reason.DAYS_BETWEEN);
        userClient = userClientPasswordService
                .updatePasswordExpirationTime(userClient);
        assertThat("Repeated password",
                userClientPasswordValidationService.validateRequest(request),
                hasItem(repeatError));
    }

}
