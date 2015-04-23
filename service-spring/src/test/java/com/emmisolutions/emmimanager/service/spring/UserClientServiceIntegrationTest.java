package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.service.*;
import com.emmisolutions.emmimanager.service.spring.security.LegacyPasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * An integration test that goes across a wired persistence layer as well
 */
public class UserClientServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientService clientService;
    
    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;
    
    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;
    
    @Resource
    EmailRestrictConfigurationService emailRestrictConfigurationService;

    @Resource
    UserClientService userClientService;

    @Resource
    UserAdminService userAdminService;
    
    @Resource
    UserClientPasswordService userClientPasswordService;

    @Resource
    PasswordEncoder passwordEncoder;

    /**
     * Create without client and login
     */
    @Test(expected = ConstraintViolationException.class)
    public void testUserCreateWithoutClient() {
        userClientService.create(new UserClient());
    }

    /**
     * Create with required values
     */
    @Test
    public void testUserCreate() {
        Client client = makeNewRandomClient();

        UserClient user = new UserClient();
        user.setClient(client);
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("flast@mail.com");
        user.setLogin("flast@mail.com");
        user = userClientService.create(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
        assertThat(user.isActivated(), is(false));
        assertThat(user.getActivationKey(), is(nullValue()));
    }

    /**
     * Reload test
     */
    @Test
    public void testUserReload() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        assertThat("Should reload the same UserClient.",
                userClientService.reload(new UserClient(
                        userClient.getId())), is(userClient));

        assertThat("Should return null", userClientService.reload(new UserClient()), is(nullValue()));
    }

    /**
     * Trying to update non-persistent UserClient
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void invalidUpdate() {
        UserClient user = new UserClient();
        userClientService.update(user);
    }

    /**
     * Ensure update is successful
     */
    @Test
    public void update() {
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setFirstName("new first name");
        userClient.setClient(makeNewRandomClient());
        userClient.setPassword("new password");
        userClient.setEmailValidated(true);
        UserClient updatedUserClient = userClientService.update(userClient);
        assertThat("version increment occurred",
                updatedUserClient.getVersion(), is(userClient.getVersion() + 1));
        assertThat("name changed", updatedUserClient.getFirstName(),
                is(userClient.getFirstName()));
        assertThat("password did not change", updatedUserClient.getPassword(),
                is(userClient.getPassword()));
        assertThat("client id not change", updatedUserClient.getClient(),
                is(userClient.getClient()));
        assertThat("email valid did not change", updatedUserClient.isEmailValidated(),
                is(userClient.isEmailValidated()));
    }

    /**
     * Make sure listing works
     */
    @Test
    public void testUserList() {
        Client client = makeNewRandomClient();
        makeNewRandomUserClient(client);

        Page<UserClient> userClients = userClientService.list(null, null);
        assertThat("userClients should contain contents",
                userClients.hasContent(), is(true));

        UserClientSearchFilter filter = new UserClientSearchFilter(client, "a");
        Page<UserClient> userClientsWithFilter = userClientService.list(null,
                filter);
        assertThat("userClients should contain contents",
                userClientsWithFilter.hasContent(), is(true));
    }

    /**
     * Ensure that login and email name conflicts are reported accurately with a
     * reason
     */
    @Test
    public void makeSureCaseInsensitiveConflictsAreFound() {
        UserClient matt = new UserClient();
        matt.setClient(makeNewRandomClient());
        matt.setFirstName(RandomStringUtils.randomAlphabetic(50));
        matt.setLastName(RandomStringUtils.randomAlphabetic(50));
        matt.setLogin("mAtT");
        matt.setEmail("mAtT@Blipso.orG");
        userClientService.create(matt);

        UserClient steve = new UserClient();
        steve.setClient(makeNewRandomClient());
        steve.setFirstName(RandomStringUtils.randomAlphabetic(50));
        steve.setLastName(RandomStringUtils.randomAlphabetic(50));
        steve.setLogin("sTeVe");
        steve.setEmail("steve@BLiPso.OrG");
        userClientService.create(steve);

        UserClient steveMatt = new UserClient();
        steveMatt.setClient(makeNewRandomClient());
        steveMatt.setFirstName(RandomStringUtils.randomAlphabetic(50));
        steveMatt.setLastName(RandomStringUtils.randomAlphabetic(50));
        steveMatt.setLogin("MATT");
        steveMatt.setEmail("STEVE@BLIPSO.ORG");

        assertThat(
                "both steve and matt should conflict with the user for the correct reason",
                userClientService.findConflictingUsers(steveMatt),
                hasItems(new UserClientService.UserClientConflict(
                                UserClientService.Reason.LOGIN, matt),
                        new UserClientService.UserClientConflict(
                                UserClientService.Reason.EMAIL, steve)));

        assertThat("no one conflicts with blanky", userClientService
                .findConflictingUsers(new UserClient()).isEmpty(), is(true));

        assertThat("no one conflicts with null", userClientService
                .findConflictingUsers(null).isEmpty(), is(true));

        assertThat("email should conflict steve on email",
                userClientService.findConflictingUsers(new UserClient() {
                    {
                        setEmail("steve@blipso.org");
                    }
                }), hasItem(new UserClientService.UserClientConflict(
                        UserClientService.Reason.EMAIL, steve)));

        assertThat("login should conflict with matt on login",
                userClientService.findConflictingUsers(new UserClient() {
                    {
                        setLogin("matt");
                    }
                }), hasItem(new UserClientService.UserClientConflict(
                        UserClientService.Reason.LOGIN, matt)));

    }

    /**
     * Ensures that a user is properly activated.
     */
    @Test
    public void activation() {
        String password = "password";

        UserClient userClient = userClientService.activate(
                new ActivationRequest(userClientService.addActivationKey(makeNewRandomUserClient(null))
                        .getActivationKey(), password));

        assertThat("user is activated",
                userClient.isActivated(),
                is(true)
        );

        assertThat("email is validated", userClient.isEmailValidated(), is(true));

        assertThat("user activation key is gone",
                userClient.getActivationKey(),
                is(nullValue())
        );
        assertThat("user can now login", login(userClient.getLogin(), password),
                is((User) userClient));
        logout();

        userClient = userClientService.reload(userClient); // login modifies the user on first login
        userClient.setEmail("anewemail@newone.com");
        UserClient newEmail = userClientService.update(userClient);
        assertThat("email is not valid due to update", newEmail.isEmailValidated(), is(false));
    }

    /**
     * When the user to be activated cannot be found null
     * should come back.
     */
    @Test
    public void badActivation() {
        assertThat("user is not activated",
                userClientService.activate(null),
                is(nullValue())
        );

        assertThat("user is not activated",
                userClientService.activate(new ActivationRequest()),
                is(nullValue())
        );
    }

    /**
     * Make sure a new activation key is created only on not activated clients
     */
    @Test
    public void activationKey() {
        UserClient userClient = userClientService.addActivationKey(makeNewRandomUserClient(null));
        assertThat("activation key is created",
                userClient.getActivationKey(),
                is(notNullValue()));

        userClient = userClientService.activate(new ActivationRequest(userClient.getActivationKey(), "whatever"));

        assertThat("activation key is not created for already activated users",
                userClientService.addActivationKey(userClient),
                is(nullValue()));
    }

    /**
     * Push the expiration time to the past and validate that reset password returns null
     */
    @Test
    public void expiredActivation() {
        UserClient userClient = userClientService.addActivationKey(makeNewRandomUserClient(null));

        UserClient expiredUserClient = userClientService.expireActivationToken(userClient);

        assertThat("should be expired activation request",
                userClientService.activate(new ActivationRequest(expiredUserClient.getActivationKey(), "***")),
                is(nullValue()));
    }

    /**
     * Shouldn't be able to expire an activation for a user that isn't found
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badExpireActivation() {
        userClientService.expireActivationToken(new UserClient());
    }

    /**
     * Add activation key to nothing should be an exception
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badActivationKey() {
        userClientService.addActivationKey(null);
    }

    /**
     * Test lock and unlock UserClient
     */
    @Test
    public void lockUserClient(){
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        
        ClientPasswordConfiguration configuration = clientPasswordConfigurationService.get(client);
        assertThat("lock attemp", configuration.getLockoutAttemps(), is(3));
        assertThat("lock duration", configuration.getLockoutReset(), is(5));
        
        userClient = userClientService.handleLoginFailure(userClient);
        assertThat("first fail don't lock", userClient.getLoginFailureCount(), is(1));
        assertThat("first fail don't lock", userClient.isAccountNonLocked(), is(true));
        
        userClient = userClientService.handleLoginFailure(userClient);
        assertThat("second fail don't lock", userClient.getLoginFailureCount(), is(2));
        assertThat("second fail don't lock", userClient.isAccountNonLocked(), is(true));
        
        userClient = userClientService.handleLoginFailure(userClient);
        assertThat("third fail lock", userClient.getLoginFailureCount(), is(3));
        assertThat("third fail lock", userClient.isAccountNonLocked(), is(false));
        assertThat("third fail lock with lock expiration timestamp", userClient.getLockExpirationDateTime(), is(notNullValue()));
        
        userClient.setLockExpirationDateTime(LocalDateTime.now(DateTimeZone.UTC).minusMinutes(2));
        userClient = userClientService.update(userClient);
        userClient = userClientService.resetUserClientLock(userClient);
        assertThat("unlock done", userClient.getLoginFailureCount(), is(0));
        assertThat("unlock done", userClient.isAccountNonLocked(), is(true));
        assertThat("unlock done", userClient.getLockExpirationDateTime(), is(nullValue()));
        
        configuration.setLockoutReset(0);
        clientPasswordConfigurationService.save(configuration);
        UserClient userClientA = makeNewRandomUserClient(client);
        
        userClientA = userClientService.handleLoginFailure(userClientA);
        assertThat("first fail don't lock", userClientA.getLoginFailureCount(), is(1));
        assertThat("first fail don't lock", userClientA.isAccountNonLocked(), is(true));
        
        userClientA = userClientService.handleLoginFailure(userClientA);
        assertThat("second fail don't lock", userClientA.getLoginFailureCount(), is(2));
        assertThat("second fail don't lock", userClientA.isAccountNonLocked(), is(true));
        
        userClientA = userClientService.handleLoginFailure(userClientA);
        assertThat("third fail lock", userClientA.getLoginFailureCount(), is(3));
        assertThat("third fail lock", userClientA.isAccountNonLocked(), is(false));
        assertThat("third fail lock with lock permanently", userClient.getLockExpirationDateTime(), is(nullValue()));

        // ensure that admin update password unlocks a locked user
        userClientA = userClientPasswordService.updatePassword(userClientA, false);
        userClientA = userClientService.handleLoginFailure(userClientA);
        assertThat("lock should be reset", userClientA.getLoginFailureCount(), is(1));
        assertThat("user should not be locked", userClientA.isAccountNonLocked(), is(true));
    }
    
    /**
     * Test email restriction
     */
    @Test
    public void testEmailRestriction(){
        Client client = makeNewRandomClient();
        UserClient newUserClient = new UserClient();
        newUserClient.setClient(client);
        newUserClient.setEmail("george@abc.com");
        // Check client without ClientRestrictConfiguration
        boolean restricted = userClientService.validateEmailAddress(newUserClient);
        assertThat("Should return true", restricted, is(true));
        
        // Check client with ClientRestrictConfiguration and isEmailConfigRestrict is false
        ClientRestrictConfiguration restrictConfiguration = new ClientRestrictConfiguration();
        restrictConfiguration.setClient(client);
        restrictConfiguration.setEmailConfigRestrict(false);
        restrictConfiguration = clientRestrictConfigurationService.create(restrictConfiguration);
        restricted = userClientService.validateEmailAddress(newUserClient);
        assertThat("Should return true", restricted, is(true));
        
        // Check client with ClientRestrictConfiguration, isEmailConfigRestrict is true and no email endings set
        restrictConfiguration.setEmailConfigRestrict(true);
        clientRestrictConfigurationService.update(restrictConfiguration);
        restricted = userClientService.validateEmailAddress(newUserClient);
        assertThat("Should return true", restricted, is(true));
        
        // Check client with ClientRestrictConfiguration, isEmailConfigRestrict is true, email endings set with valid email
        EmailRestrictConfiguration emailEndingA = new EmailRestrictConfiguration();
        emailEndingA.setClient(client);
        emailEndingA.setEmailEnding("abc.com");
        emailRestrictConfigurationService.create(emailEndingA);
        EmailRestrictConfiguration emailEndingB = new EmailRestrictConfiguration();
        emailEndingB.setClient(client);
        emailEndingB.setEmailEnding("bcd.com");
        emailRestrictConfigurationService.create(emailEndingB);
        restricted = userClientService.validateEmailAddress(newUserClient);
        assertThat("Should return true", restricted, is(true));
        
        newUserClient.setEmail("george@aa.abc.com");
        restricted = userClientService.validateEmailAddress(newUserClient);
        assertThat("Should return true", restricted, is(true));
        
        newUserClient.setEmail("george@aa.bb.abc.com");
        restricted = userClientService.validateEmailAddress(newUserClient);
        assertThat("Should return true", restricted, is(true));
        
        // Check client with ClientRestrictConfiguration, isEmailConfigRestrict is true, email endings set with invalid email
        newUserClient.setEmail("george@apple.com");
        restricted = userClientService.validateEmailAddress(newUserClient);
        assertThat("Should return false", restricted, is(false));

        UserClient inDb = makeNewRandomUserClient(client);
        inDb.setEmail("george@apple.com");
        inDb.setClient(null);
        assertThat("should load from the database and fail", userClientService.validateEmailAddress(inDb), is(false));
    }

    /**
     * Make sure our exception cases are taken care of
     */
    @Test
    public void testUnCommonUserClients(){
        assertThat("email should be 'valid'", userClientService.validateEmailAddress(null), is(true));
        assertThat("email should be 'valid'", userClientService.validateEmailAddress(new UserClient()), is(true));
    }
    
    @Test
    public void expireUserClientCredential(){
        UserClient userClient = makeNewRandomUserClient(null);
        assertThat("userClient credential is not expired", userClient.isCredentialsNonExpired(), is(true));
        
        userClient = userClientService.expireUserClientCredential(userClient);
        assertThat("userClient credential is expired", userClient.isCredentialsNonExpired(), is(false));
    }

    /**
     * test not now functionality
     */
    @Test
    public void testNotNow(){
        UserClient userClient = new UserClient();
        userClient.setClient(makeNewRandomClient());
        userClient.setFirstName(RandomStringUtils.randomAlphabetic(50));
        userClient.setLastName(RandomStringUtils.randomAlphabetic(50));
        userClient.setLogin(RandomStringUtils.randomAlphabetic(50));
        userClient.setEmail("tottally@randomEmail.com");
        userClientService.create(userClient);
        userClient = userClientService.saveNotNowExpirationTime(userClient.getId());
        assertThat("userClient should have not not expiration date defined",
                userClient.getNotNowExpirationTime(), notNullValue());
    }

    /**
     * test validateActivationToken is valid
     */
    @Test
    public void testValidateActivationTokenIsValid(){
        boolean isValid = userClientService.validateActivationToken(
                new ActivationRequest(userClientService.addActivationKey(makeNewRandomUserClient(null))
                        .getActivationKey(), null));
        assertThat("token is valid", isValid, is(true));
    }

    /**
     * test validateActivationToken is invalid
     */
    @Test
    public void testValidateActivationTokenIsInvalid(){
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setActivationExpirationDateTime(LocalDateTime.now(DateTimeZone.UTC).minusDays(1));
        userClient.setActivationKey(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(40))
                        .substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        ActivationRequest activationRequest = new ActivationRequest(userClient.getActivationKey(), null);
        boolean isValid = userClientService.validateActivationToken(activationRequest);

        assertThat("token is invalid", isValid, is(false));
    }

    /**
     * return false if activation request is null
     */
    @Test
    public void testValidateActivationTokenNullRequest(){
        boolean isValid = userClientService.validateActivationToken(null);
        assertThat("return false for null activation request", isValid, is(false));
    }

    /**
     * return false if user client is null
     */
    @Test
    public void testValidateActivationTokenNullUserClient(){
        boolean isValid = userClientService.validateActivationToken(new ActivationRequest("invalidToken",null));
        assertThat("return false for null user client", isValid, is(false));
    }
}
