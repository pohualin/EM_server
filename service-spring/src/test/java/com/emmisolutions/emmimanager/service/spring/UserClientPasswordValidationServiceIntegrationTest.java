package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService.UserClientPasswordValidationError;

/**
 * Integration test for the UserClientValidationService
 */
public class UserClientPasswordValidationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    UserClientPasswordService userClientPasswordService;

    @Resource
    UserClientPasswordValidationService userClientPasswordValidationService;

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
}
