package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
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
    }

    @Test
    public void testUserUpdate() {
	UserClient user = new UserClient();
	userClientService.update(user);
    }

    @Test
    public void testUserList() {
	Client client = makeNewRandomClient();
	makeNewRandomUserClient(client);

	Page<UserClient> userClients = userClientService.list(null,
		client.getId(), null);
	assertThat("userClients should contain contents",
		userClients.hasContent(), is(true));

	UserClientSearchFilter filter = new UserClientSearchFilter("a");
	Page<UserClient> userClientsWithFilter = userClientService.list(null,
		client.getId(), filter);
	assertThat("userClients should contain contents",
		userClientsWithFilter.hasContent(), is(true));
    }
}
