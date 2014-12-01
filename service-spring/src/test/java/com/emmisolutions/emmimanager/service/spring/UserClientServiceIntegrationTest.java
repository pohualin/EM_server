package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserService;

/**
 * An integration test that goes across a wired persistence layer as well
 */
public class UserClientServiceIntegrationTest extends BaseIntegrationTest {

	@Resource
	ClientService clientService;

	@Resource
	UserClientService userClientService;

	@Resource
	UserService userService;

	/**
	 * Create without client and login
	 */
	@Test(expected = ConstraintViolationException.class)
	public void testUserCreateWithoutLogin() {
		userClientService
				.save(new UserClient());
	}

	/**
	 * Create with required values
	 */
	@Test
	public void testUserCreate() {
		Client client = makeClient("UserClientService", "UserClientService");
		clientService.create(client);

		UserClient user = new UserClient();
		user.setClient(client);
		user.setLogin("flast@mail.com");
		user = userClientService.save(user);
		assertThat(user.getId(), is(notNullValue()));
		assertThat(user.getVersion(), is(notNullValue()));
	}

	protected Client makeClient(String clientName, String username) {
		Client client = new Client();
		client.setType(new ClientType(4l));
		client.setContractStart(LocalDate.now());
		client.setContractEnd(LocalDate.now().plusYears(1));
		client.setName(clientName);
		client.setContractOwner(userService.save(new UserAdmin(username, "pw")));
		client.setSalesForceAccount(new SalesForce(RandomStringUtils
				.randomAlphanumeric(18)));
		return client;
	}

}
