package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserClientRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration test for UserClientPersistence
 */
public class UserClientPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientRepository userClientRepository;

    /**
     * valid create
     */
    @Test
    public void testCreate() {
        Client client = makeNewRandomClient();

        UserClient user = new UserClient();
        user.setClient(client);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setLogin("flast@mail.com");
        user.setEmail("flast@gmail.com");
        user = userClientPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        UserClient user1 = userClientRepository.findOne(user.getId());
        assertThat("the users saved should be the same as the user fetched",
                user, is(user1));
    }

    /**
     * No first name
     */
    @Test(expected = ConstraintViolationException.class)
    public void testBadFirstNameCreate() {
        Client client = makeNewRandomClient();
        UserClient user = new UserClient();
        user.setLastName(RandomStringUtils.randomAlphabetic(10));
        user.setLogin(RandomStringUtils.randomAlphabetic(10));
        user.setEmail("abc@gmail.com");
        user.setClient(client);
        userClientPersistence.saveOrUpdate(user);
    }

    /**
     * No Last name
     */
    @Test(expected = ConstraintViolationException.class)
    public void testBadLastNameCreate() {
        Client client = makeNewRandomClient();
        UserClient user = new UserClient();
        user.setFirstName(RandomStringUtils.randomAlphabetic(10));
        user.setLogin(RandomStringUtils.randomAlphabetic(10));
        user.setEmail("abc@gmail.com");
        user.setClient(client);
        userClientPersistence.saveOrUpdate(user);
    }

    /**
     * No login
     */
    @Test(expected = ConstraintViolationException.class)
    public void testBadLoginCreate() {
        Client client = makeNewRandomClient();
        UserClient user = new UserClient();
        user.setLastName(RandomStringUtils.randomAlphabetic(10));
        user.setFirstName(RandomStringUtils.randomAlphabetic(10));
        user.setEmail("abc@gmail.com");
        user.setClient(client);
        userClientPersistence.saveOrUpdate(user);
    }

    /**
     * Invalid email
     */
    @Test
    public void testBadEmailCreate() {
        Client client = makeNewRandomClient();
        UserClient user = new UserClient();
        user.setFirstName(RandomStringUtils.randomAlphabetic(10));
        user.setLastName(RandomStringUtils.randomAlphabetic(10));
        user.setLogin(RandomStringUtils.randomAlphabetic(10));
        user.setClient(client);
        user = userClientPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
    }

    /**
     * make sure we can search and filter
     */
    @Test
    public void testList() {
        Client client = makeNewRandomClient();
        Client clientA = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        userClient.setActive(false);

        UserClientSearchFilter filter = new UserClientSearchFilter(client, "");
        Page<UserClient> userClients = userClientPersistence.list(null, filter);
        assertThat("returned page of UserClient should not be empty",
                userClients.hasContent(), is(true));

        UserClientSearchFilter filterA = new UserClientSearchFilter(clientA, "");
        Page<UserClient> userClientsA = userClientPersistence.list(null,
                filterA);
        assertThat("returned page of UserClient should be empty",
                userClientsA.hasContent(), is(false));

        UserClientSearchFilter realFilter = new UserClientSearchFilter(client, "a");
        Page<UserClient> userClientsWithFilter = userClientPersistence.list(
                null, realFilter);
        assertThat("returned page of UserClient should not be empty",
                userClientsWithFilter.hasContent(), is(true));

        UserClientSearchFilter nullClientIdFilter = new UserClientSearchFilter(
                null, "a");
        Page<UserClient> userClientsWithNullClientIdFilter = userClientPersistence
                .list(null, nullClientIdFilter);
        assertThat("returned page of UserClient should not be empty",
                userClientsWithNullClientIdFilter.hasContent(), is(true));

        Page<UserClient> userClientsWithPageableAndFilter = userClientPersistence
                .list(new PageRequest(0, 10), realFilter);
        assertThat("returned page of UserClient should not be empty",
                userClientsWithPageableAndFilter.hasContent(), is(true));

        // make sure status filter is working
        assertThat("active status should return nothing",
                userClientPersistence.list(new PageRequest(0, 10), new UserClientSearchFilter(
                        client, UserClientSearchFilter.StatusFilter.ACTIVE_ONLY, "a")).getTotalElements(),
                is(0l));

        assertThat("inactive status should return results",
                userClientPersistence.list(new PageRequest(0, 10), new UserClientSearchFilter(
                        new Client(client.getId()), UserClientSearchFilter.StatusFilter.INACTIVE_ONLY, "a")),
                hasItem(userClient));
    }

    /**
     * Ensure reloading of users works
     */
    @Test
    public void testReload() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);

        UserClient userClientNull = userClientPersistence.reload(null);
        assertThat("return null", userClientNull, is(nullValue()));

        assertThat("reload same UserClient object",
                userClientPersistence.reload(userClient),
                is(userClient));
    }

    /**
     * Make sure that two users cannot have the same email
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void ensureUniqueEmail() {
        UserClient matt = new UserClient();
        matt.setClient(makeNewRandomClient());
        matt.setFirstName(RandomStringUtils.randomAlphabetic(50));
        matt.setLastName(RandomStringUtils.randomAlphabetic(50));
        matt.setLogin("mAtT");
        matt.setEmail("mAtT@Blipso.orG");
        userClientPersistence.saveOrUpdate(matt);

        UserClient steve = new UserClient();
        steve.setClient(makeNewRandomClient());
        steve.setFirstName(RandomStringUtils.randomAlphabetic(50));
        steve.setLastName(RandomStringUtils.randomAlphabetic(50));
        steve.setLogin("sTeVe");
        steve.setEmail("matt@blipso.org");
        userClientPersistence.saveOrUpdate(steve);
    }

    /**
     * Ensure that login and email name conflicts are reported
     */
    @Test
    public void makeSureCaseInsensitiveConflictsAreFound() {
        UserClient matt = new UserClient();
        matt.setClient(makeNewRandomClient());
        matt.setFirstName(RandomStringUtils.randomAlphabetic(50));
        matt.setLastName(RandomStringUtils.randomAlphabetic(50));
        matt.setLogin("mAtT");
        matt.setEmail("mAtT@Blipso.orG");
        userClientPersistence.saveOrUpdate(matt);

        UserClient steve = new UserClient();
        steve.setClient(makeNewRandomClient());
        steve.setFirstName(RandomStringUtils.randomAlphabetic(50));
        steve.setLastName(RandomStringUtils.randomAlphabetic(50));
        steve.setLogin("sTeVe");
        steve.setEmail("steve@BLiPso.OrG");
        userClientPersistence.saveOrUpdate(steve);

        UserClient steveMatt = new UserClient();
        steveMatt.setClient(makeNewRandomClient());
        steveMatt.setFirstName(RandomStringUtils.randomAlphabetic(50));
        steveMatt.setLastName(RandomStringUtils.randomAlphabetic(50));
        steveMatt.setLogin("MATT");
        steveMatt.setEmail("STEVE@BLIPSO.ORG");

        assertThat("both steve and matt should conflict with the user",
                userClientPersistence.findConflictingUsers(steveMatt),
                hasItems(steve, matt));

        assertThat("no one conflicts with blanky", userClientPersistence
                .findConflictingUsers(new UserClient()).isEmpty(), is(true));

        assertThat("no one conflicts with null", userClientPersistence
                .findConflictingUsers(null).isEmpty(), is(true));

        assertThat("email should conflict steve",
                userClientPersistence.findConflictingUsers(new UserClient() {
                    {
                        setEmail("steve@blipso.org");
                    }
                }), hasItem(steve));

        assertThat("login should conflict with matt",
                userClientPersistence.findConflictingUsers(new UserClient() {
                    {
                        setLogin("matt");
                    }
                }), hasItem(matt));

        assertThat("matt should not conflict with itself",
                userClientPersistence.findConflictingUsers(matt).isEmpty(),
                is(true));

    }

    /**
     * Happy path for activation key setup
     */
    @Test
    public void findByActivationKey() {
        UserClient userClient = makeNewRandomUserClient(null);
        userClient.setActivationKey("whatever");
        userClientPersistence.saveOrUpdate(userClient);
        assertThat("find by activation key should work",
                userClientPersistence.findByActivationKey(userClient.getActivationKey()),
                is(userClient)
        );
    }

    /**
     * Null activation code searching
     */
    @Test
    public void findByNullActivationKey() {
        UserClient userClient = makeNewRandomUserClient(null);
        assertThat("no activation key on user should return null value",
                userClientPersistence.findByActivationKey(userClient.getActivationKey()),
                is(nullValue())
        );
    }

}
