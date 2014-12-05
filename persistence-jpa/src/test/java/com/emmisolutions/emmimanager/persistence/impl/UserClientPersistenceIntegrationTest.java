package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

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

    @Test
    public void testList() {
	Client client = makeNewRandomClient();
	Client clientA = makeNewRandomClient();
	makeNewRandomUserClient(client);

	Page<UserClient> userClients = userClientPersistence.list(null, client,
		null);
	assertThat("returned page of UserClient should not be empty",
		userClients.hasContent(), is(true));

	Page<UserClient> userClientsA = userClientPersistence.list(null,
		clientA, null);
	assertThat("returned page of UserClient should not be empty",
		userClientsA.hasContent(), is(false));

	UserClientSearchFilter filter = new UserClientSearchFilter("a");
	Page<UserClient> userClientsWithFilter = userClientPersistence.list(
		null, client, filter);
	assertThat("returned page of UserClient should not be empty",
		userClientsWithFilter.hasContent(), is(true));
    }
}
