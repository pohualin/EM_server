package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.SalesForce;
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

	@Before
	public void init() {
		superAdmin = userPersistence.reload("super_admin");
		clientType = clientTypeRepository.getOne(1l);
	}

	/**
	 * valid create
	 */
	@Test
	public void testCreate() {
		Client client = makeClient();
		clientPersistence.save(client);

		UserClient user = new UserClient();
		user.setClient(client);
		user.setLogin("flast@mail.com");
		user = userClientPersistence.saveOrUpdate(user);
		assertThat(user.getId(), is(notNullValue()));
		assertThat(user.getVersion(), is(notNullValue()));

		UserClient user1 = userClientRepository.findOne(user.getId());
		assertThat("the users saved should be the same as the user fetched",
				user, is(user1));
	}

	private Client makeClient() {
		Client client = new Client();
		client.setActive(false);
		client.setName("Test Client");
		client.setType(clientType);
		client.setRegion(new ClientRegion(1l));
		client.setTier(new ClientTier(3l));
		client.setContractOwner(superAdmin);
		client.setContractStart(LocalDate.now());
		client.setContractEnd(LocalDate.now().plusYears(2));
		client.setSalesForceAccount(new SalesForce(RandomStringUtils
				.randomAlphanumeric(18)));
		return client;
	}
}
