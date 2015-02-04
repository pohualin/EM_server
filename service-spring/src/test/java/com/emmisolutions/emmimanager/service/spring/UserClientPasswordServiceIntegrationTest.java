package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientService;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
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
     * make sure login works but the credentials should be expired.
     */
    @Test(expected = CredentialsExpiredException.class)
    public void adminChangePassword() {
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


    /**
     * Calling expired password change shouldn't do anything when
     * the user does not have expired credentials.
     */
    @Test
    public void callChangeOnExpiredUserCredentials() {
        final String pw = "matt", newPassword = "mateo";
        final UserClient user = makeNewRandomUserClient(null);

        user.setPassword(pw);
        // set the password as an admin
        userClientPasswordService.updatePassword(user);
        try {
            // try to login with the updated admin password
            login(user.getLogin(), pw);
            fail("should not have been able to login with expired credentials");
        } catch (CredentialsExpiredException e) {
            // change the password which is expired
            userClientPasswordService.changeExpiredPassword(new ExpiredPasswordChangeRequest() {{
                setExistingPassword(pw);
                setLogin(user.getLogin());
                setNewPassword(newPassword);
            }});
        }
        // make sure we can login
        assertThat("can login with new password", login(user.getLogin(), newPassword), is(equalTo((User) user)));
        logout();

        // try to change the password to something else using the old existing password
        userClientPasswordService.changeExpiredPassword(new ExpiredPasswordChangeRequest() {{
            setExistingPassword(pw);
            setLogin(user.getLogin());
            setNewPassword("something else");
        }});
        // make sure the update didn't happen
        assertThat("still can login with password we used earlier", login(user.getLogin(), newPassword), is(equalTo((User) user)));
        logout();

    }

    /**
     * None of the edge cases should stack out
     */
    @Test
    public void expiredPasswordEdgeCases() {
        final String empty = "", notEmpty = "a";

        // null request
        userClientPasswordService.changeExpiredPassword(null);

        // empty login
        userClientPasswordService.changeExpiredPassword(new ExpiredPasswordChangeRequest() {{
            setExistingPassword(notEmpty);
            setLogin(empty);
            setNewPassword(notEmpty);
        }});

        // empty existing password
        userClientPasswordService.changeExpiredPassword(new ExpiredPasswordChangeRequest() {{
            setExistingPassword(empty);
            setLogin(notEmpty);
            setNewPassword(notEmpty);
        }});

        // empty new password
        userClientPasswordService.changeExpiredPassword(new ExpiredPasswordChangeRequest() {{
            setExistingPassword(notEmpty);
            setLogin(notEmpty);
            setNewPassword(empty);
        }});

        // login not found
        userClientPasswordService.changeExpiredPassword(new ExpiredPasswordChangeRequest() {{
            setExistingPassword(notEmpty);
            setLogin("no_way_this_exists");
            setNewPassword(empty);
        }});
    }

    /**
     * Ensures that a user is properly activated.
     */
    @Test
    public void passwordReset() {
        String password = "password";

        UserClient userClient = userClientPasswordService.resetPassword(
                new ResetPasswordRequest(userClientService.addResetTokenTo(makeNewRandomUserClient(null))
                        .getPasswordResetToken(), password));

        assertThat("user has reset password",
                userClient.getPasswordResetToken(),
                is(nullValue())
        );
        assertThat("user can now login", login(userClient.getLogin(), password),
                is((User) userClient));
        logout();
    }

    /**
     * When the user to be reset cannot be found, null
     * should come back.
     */
    @Test
    public void badPasswordReset() {
        assertThat("user is not reset",
                userClientPasswordService.resetPassword(null),
                is(nullValue())
        );

        assertThat("user is not reset",
                userClientPasswordService.resetPassword(new ResetPasswordRequest()),
                is(nullValue())
        );
    }
}
