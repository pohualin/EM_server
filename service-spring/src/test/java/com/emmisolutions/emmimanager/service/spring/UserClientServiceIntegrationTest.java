package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserAdminService;
import com.emmisolutions.emmimanager.service.UserClientService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

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
    UserClientService userClientService;

    @Resource
    UserAdminService userAdminService;

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
        UserClient updatedUserClient = userClientService.update(userClient);
        assertThat("version increment occurred",
                updatedUserClient.getVersion(), is(userClient.getVersion() + 1));
        assertThat("name changed", updatedUserClient.getFirstName(),
                is(userClient.getFirstName()));
        assertThat("password did not change", updatedUserClient.getPassword(),
                is(userClient.getPassword()));
        assertThat("client id not change", updatedUserClient.getClient(),
                is(userClient.getClient()));
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
        assertThat("user activation key is gone",
                userClient.getActivationKey(),
                is(nullValue())
        );
        assertThat("user can now login", login(userClient.getLogin(), password),
                is((User) userClient));
        logout();
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
     * Make sure a new activation key is created
     */
    public void activationKey() {
        UserClient userClient = makeNewRandomUserClient(null);
        assertThat("activation key is created",
                userClientService.addActivationKey(userClient).getActivationKey(),
                is(notNullValue()));

        userClientService.activate(new ActivationRequest(userClient.getActivationKey(), "whatever"));

        assertThat("activation key is not created for already activated users",
                userClientService.addActivationKey(userClient).getActivationKey(),
                is(nullValue()));
    }

    /**
     * Add activation key to nothing should be an exception
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badActivationKey() {
        userClientService.addActivationKey(null);
    }

}
