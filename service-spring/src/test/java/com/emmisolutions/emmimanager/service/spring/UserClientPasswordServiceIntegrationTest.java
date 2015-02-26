package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientService;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
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
    ClientPasswordConfigurationService clientPasswordConfigurationService;
    
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

        UserClient userClient = userClientPasswordService.addResetTokenTo(makeNewRandomUserClient(null));

        assertThat("user does not have a validated email", userClient.isEmailValidated(), is(false));

        UserClient afterReset = userClientPasswordService.resetPassword(
                new ResetPasswordRequest(userClient.getPasswordResetToken(), password));

        assertThat("user has reset password",
                afterReset.getPasswordResetToken(),
                is(nullValue())
        );

        assertThat("user now has an validated email", afterReset.isEmailValidated(), is(true));

        assertThat("user can now login", login(afterReset.getLogin(), password),
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

    /**
     * Make sure a new password reset key is created
     */
    @Test
    public void resetToken() {
        UserClient userClient = makeNewRandomUserClient(null);
        assertThat("reset token is created",
                userClientPasswordService.addResetTokenTo(userClient).getPasswordResetToken(),
                is(notNullValue()));
    }

    /**
     * Add reset token to nothing should be an exception
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badResetToken() {
        userClientPasswordService.addResetTokenTo(null);
    }

    /**
     * Make sure a forgot password adds a reset key when
     * the email corresponds to a user client
     */
    @Test
    public void forgotPassword() {
        Client client = makeNewRandomClient();
        ClientPasswordConfiguration configuration = clientPasswordConfigurationService.get(client);
        assertThat("allowed user self reset password is true", configuration.isPasswordReset(), is(true));
        
        UserClient userClient = makeNewRandomUserClient(client);
        assertThat("reset token is created",
                userClientPasswordService.forgotPassword(userClient.getEmail()).getPasswordResetToken(),
                is(notNullValue()));

        assertThat("reset token is not created",
                userClientPasswordService.forgotPassword(null),
                is(nullValue()));
        
        configuration.setPasswordReset(false);
        configuration = clientPasswordConfigurationService.save(configuration);
        assertThat("allowed user self reset password is false", configuration.isPasswordReset(), is(false));
        
        assertThat("reset token is not created",
                userClientPasswordService.forgotPassword(userClient.getEmail()).getPasswordResetToken(),
                is(nullValue()));
        
        assertThat("reset token is not created",
                userClientPasswordService
                        .forgotPassword("notfound@notfound.com"),
                is(nullValue()));
    }

    /**
     * Push the expiration time to the past and validate that reset password returns null
     */
    @Test
    public void expiredPasswordChange() {
        UserClient userClient = userClientPasswordService.forgotPassword(makeNewRandomUserClient(null).getEmail());

        UserClient expiredClient = userClientPasswordService.expireResetToken(userClient);

        assertThat("should be expired",
                userClientPasswordService.resetPassword(new ResetPasswordRequest(expiredClient.getPasswordResetToken(), "***")),
                is(nullValue()));
    }

    /**
     * Make sure the reset token can be expired properly
     */
    @Test
    public void expireResetToken() {
        UserClient userClient = userClientPasswordService.forgotPassword(makeNewRandomUserClient(null).getEmail());
        LocalDateTime now = LocalDateTime.now(DateTimeZone.UTC);
        assertThat("reset token is created", userClient.getPasswordResetToken(), is(notNullValue()));
        assertThat("reset timestamp is created", userClient.getPasswordResetExpirationDateTime(),
                is(notNullValue()));

        UserClient expiredClient = userClientPasswordService.expireResetToken(userClient);
        assertThat("reset token is the same",
                expiredClient.getPasswordResetToken(),
                is(userClient.getPasswordResetToken()));
        assertThat("reset timestamp should still exist but should be behind the current time",
                expiredClient.getPasswordResetExpirationDateTime()
                        .isBefore(now.minusHours(UserClientPasswordService.RESET_TOKEN_HOURS_VALID)),
                is(true));
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badExpireReset() {
        userClientPasswordService.expireResetToken(new UserClient());
    }


    /**
     * Happy path for finding password policy from a reset token
     */
    @Test
    public void passwordPolicyFromResetToken() {
        ClientPasswordConfiguration existingPolicy = makeNewRandomClientPasswordConfiguration(null);
        // reset a user
        UserClient userClient = userClientPasswordService
                .forgotPassword(makeNewRandomUserClient(existingPolicy.getClient()).getEmail());

        ClientPasswordConfiguration policy =
                userClientPasswordService.findPasswordPolicyUsingResetToken(userClient.getPasswordResetToken());
        assertThat("existing policy was found", policy, is(existingPolicy));
    }

    /**
     * This one should 'find' a password policy even though there
     * is no user associated to the reset token
     */
    @Test
    public void passwordPolicyFromNullResetToken() {
        ClientPasswordConfiguration policy =
                userClientPasswordService.findPasswordPolicyUsingResetToken(null);
        assertThat("a policy was found", policy, is(notNullValue()));
        assertThat("the policy was created and not persistent", policy.getId(), is(nullValue()));
    }

    /**
     * Happy path for finding password policy from an activation token
     */
    @Test
    public void passwordPolicyFromActivationToken() {
        ClientPasswordConfiguration existingPolicy = makeNewRandomClientPasswordConfiguration(null);
        // activate a user
        UserClient userClient = userClientService.addActivationKey(makeNewRandomUserClient(existingPolicy.getClient()));

        ClientPasswordConfiguration policy =
                userClientPasswordService.findPasswordPolicyUsingActivationToken(userClient.getActivationKey());
        assertThat("existing policy was found", policy, is(existingPolicy));
    }
    
    @Test
    public void testValidateNewPassword(){
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        ExpiredPasswordChangeRequest req = new ExpiredPasswordChangeRequest();
        req.setLogin(userClient.getLogin());
        req.setNewPassword("");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));
        
        req.setNewPassword("abc");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));
        
        req.setNewPassword("abcABC");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));

        req.setNewPassword("abcABC123");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
        
        req.setNewPassword("abcABC123[]!");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
        
        ClientPasswordConfiguration configuration = clientPasswordConfigurationService.get(client);
        configuration = clientPasswordConfigurationService.save(configuration);
        
        req.setNewPassword("abcABC");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));
        
        req.setNewPassword("abcABC123");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
        
        req.setNewPassword("abcABC123[]!");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
        
        configuration.setSpecialChars(true);
        configuration = clientPasswordConfigurationService.save(configuration);
        
        req.setNewPassword("abcABC");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));
        
        req.setNewPassword("abcABC123");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));
        
        req.setNewPassword("abcABC123[]!");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
        
        configuration.setLowercaseLetters(false);
        configuration = clientPasswordConfigurationService.save(configuration);
        
        req.setNewPassword("abcABC123");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));
        
        req.setNewPassword("ABCABC123[]!");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
        
        configuration.setUppercaseLetters(false);
        configuration = clientPasswordConfigurationService.save(configuration);
        
        req.setNewPassword("abcABC123");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));
        
        req.setNewPassword("123123123[]!");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
        
        configuration.setNumbers(false);
        configuration = clientPasswordConfigurationService.save(configuration);
        
        req.setNewPassword("abcABC123");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));
        
        req.setNewPassword("!!!!!!!!![]!");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
    }
    
    @Test
    public void testValidateNewResetPassword(){
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        userClient.setEmail("apple@abc.com");
        userClientService.update(userClient);
        
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setResetToken(userClientPasswordService.forgotPassword("apple@abc.com").getPasswordResetToken());
        
        req.setNewPassword("abcABC");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));

        req.setNewPassword("abcABC123");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
        
        req.setNewPassword("abcABC123[]!");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
    }
    
    @Test
    public void testValidateNewActivatePassword(){
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        userClient = userClientService.addActivationKey(new UserClient(userClient.getId()));
        
        ActivationRequest req = new ActivationRequest();
        req.setActivationToken(userClient.getActivationKey());
        
        req.setNewPassword("abcABC");
        assertThat("Should not match", userClientPasswordService.validateNewPassword(req), is(false));

        req.setNewPassword("abcABC123");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
        
        req.setNewPassword("abcABC123[]!");
        assertThat("Should match", userClientPasswordService.validateNewPassword(req), is(true));
    }
}
