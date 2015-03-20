package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientValidationEmailService;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration test for UserClientValidateEmailService
 */
public class UserClientValidateEmailServiceIntegrationTest extends BaseIntegrationTest {
    @Resource
    UserClientService userClientService;

    @Resource
    UserClientValidationEmailService userClientValidationEmailService;

    @Test
    public void testAddValidationTokenTo() {
        Client client = makeNewRandomClient();
        UserClient user = makeNewRandomUserClient(client);
        UserClient saveUser = userClientValidationEmailService.addValidationTokenTo(user);
        assertThat(saveUser.getValidationToken(), is(notNullValue()));
        assertThat(saveUser.getValidationExpirationDateTime(), is(notNullValue()));

    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testAddValidationTokenToHasError() {
        UserClient user = new UserClient();
        userClientValidationEmailService.addValidationTokenTo(null);
    }

    @Test
    public void testValidation() {
        Client client = makeNewRandomClient();
        UserClient user = makeNewRandomUserClient(client);
        user = userClientValidationEmailService.addValidationTokenTo(user);
        String validationToken = user.getValidationToken();
        user = userClientValidationEmailService.validateEmailToken(validationToken);
        assertThat(user.isEmailValidated(), is(true));
        assertThat(user.getValidationToken(), is(nullValue()));
    }

    @Test
    public void testValidationIsNull() {
        UserClient user = userClientValidationEmailService.validateEmailToken(null);
        assertThat(user, is(nullValue()));
    }
}
