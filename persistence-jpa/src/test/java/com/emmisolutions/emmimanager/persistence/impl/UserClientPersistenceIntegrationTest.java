package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserClientRepository;

/**
 * Integration test for UserClientPersistence
 */
public class UserClientPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    ClientTypeRepository clientTypeRepository;

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserPersistence userPersistence;

    @Resource
    UserClientRepository userClientRepository;

    ClientType clientType;

    UserAdmin superAdmin;

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

    @Test(expected = ConstraintViolationException.class)
    public void testBadFirstNameCreate() {
	Client client = makeNewRandomClient();
	UserClient user = new UserClient();
	user.setLastName(RandomStringUtils.randomAlphabetic(10));
	user.setLogin(RandomStringUtils.randomAlphabetic(10));
	user.setEmail("abc@gmail.com");
	user.setClient(client);
	user = userClientPersistence.saveOrUpdate(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testBadLastNameCreate() {
	Client client = makeNewRandomClient();
	UserClient user = new UserClient();
	user.setFirstName(RandomStringUtils.randomAlphabetic(10));
	user.setLogin(RandomStringUtils.randomAlphabetic(10));
	user.setEmail("abc@gmail.com");
	user.setClient(client);
	user = userClientPersistence.saveOrUpdate(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testBadLoginCreate() {
	Client client = makeNewRandomClient();
	UserClient user = new UserClient();
	user.setLastName(RandomStringUtils.randomAlphabetic(10));
	user.setFirstName(RandomStringUtils.randomAlphabetic(10));
	user.setEmail("abc@gmail.com");
	user.setClient(client);
	user = userClientPersistence.saveOrUpdate(user);
    }

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

    @Test
    public void testList() {
	Client client = makeNewRandomClient();
	Client clientA = makeNewRandomClient();
	makeNewRandomUserClient(client);

	UserClientSearchFilter filter = new UserClientSearchFilter(
		client.getId(), "");
	Page<UserClient> userClients = userClientPersistence.list(null, filter);
	assertThat("returned page of UserClient should not be empty",
		userClients.hasContent(), is(true));

	UserClientSearchFilter filterA = new UserClientSearchFilter(
		clientA.getId(), "");
	Page<UserClient> userClientsA = userClientPersistence.list(null,
		filterA);
	assertThat("returned page of UserClient should be empty",
		userClientsA.hasContent(), is(false));

	UserClientSearchFilter realFilter = new UserClientSearchFilter(
		client.getId(), "a");
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
    }

    @Test
    public void testReload() {
	Client client = makeNewRandomClient();
	UserClient userClient = makeNewRandomUserClient(client);

	UserClient userClientNull = userClientPersistence.reload(null);
	assertThat("return null", userClientNull == null, is(true));

	UserClient userClientA = userClientPersistence.reload(userClient
		.getId());
	assertThat("reload same UserClient object",
		userClient.getId() == userClientA.getId(), is(true));
    }
}
