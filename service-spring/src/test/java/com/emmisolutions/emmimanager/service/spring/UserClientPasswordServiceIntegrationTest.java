package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientService;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.BadCredentialsException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Integration test for the AdminPasswordService
 */
public class UserClientPasswordServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientPasswordService userClientPasswordService;

    @Resource
    UserClientService userClientService;

    /**
     * Create a user, ensure login doesn't work, change the password,
     * make sure login works
     */
    @Test
    public void changeAPassword() {
        String newPassword = "brand new password";
        UserClient userClient = makeNewRandomUserClient(null);
        try {
            login(userClient.getLogin(), newPassword);
            fail("should not be able to login with the new password until it has been changed");
        } catch (BadCredentialsException bce) {
            userClient.setPassword(newPassword);
            userClientPasswordService.updatePassword(userClient);
            assertThat("can login with the new user client", login(userClient.getLogin(), newPassword),
                    is((User) userClient));
        } finally {
            logout();
        }
    }

    /**
     * Trying to update a non-user should throw an api exception
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badUpdate() {
        userClientPasswordService.updatePassword(null);
    }

    /**
     * Update should be successful but login shouldn't work
     */
    @Test(expected = BadCredentialsException.class)
    public void updateToNullPassword() {
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setPassword(null);
        userClientPasswordService.updatePassword(userClient);
        login(userClient.getLogin(), null);
    }
}
