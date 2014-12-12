package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientUserClientRoleService;

/**
 * An integration test that goes across a wired persistence layer as well
 */
public class UserClientUserClientRoleServiceIntegrationTest extends
	BaseIntegrationTest {

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientUserClientRoleService userClientUserClientRoleService;

    /**
     * Create with required values
     */
    @Test
    public void testUserClientUserClientRoleCreate() {
	Client client = makeNewRandomClient();
	UserClient userClient = makeNewRandomUserClient(client);
	UserClientRole userClientRole = makeNewRandomUserClientRole(client);

	UserClientUserClientRole entity = new UserClientUserClientRole();
	entity.setUserClient(userClient);
	entity.setUserClientRole(userClientRole);
	userClientUserClientRoleService.create(entity);
    }

    @Test
    public void testFindByUserClient() {
	Client client = makeNewRandomClient();
	UserClient userClient = makeNewRandomUserClient(client);
	UserClientRole userClientRole = makeNewRandomUserClientRole(client);
	UserClientUserClientRole entity = new UserClientUserClientRole();
	entity.setUserClient(userClient);
	entity.setUserClientRole(userClientRole);
	userClientUserClientRoleService.create(entity);

	Page<UserClientUserClientRole> ucucr = userClientUserClientRoleService
		.findByUserClient(userClient, null);
	assertThat("Should return a page of UserClientUserClientRole.",
		ucucr.hasContent(), is(true));

	Page<UserClientUserClientRole> ucucrA = userClientUserClientRoleService
		.findByUserClient(userClient, new PageRequest(0, 10));
	assertThat("Should return a page of UserClientUserClientRole.",
		ucucrA.hasContent(), is(true));

	// TODO asserts
	Page<UserClientUserClientRole> ucucrB = userClientUserClientRoleService
		.findByUserClient(new UserClient(), null);
    }

    @Test
    public void testReload() {
	Client client = makeNewRandomClient();
	UserClient userClient = makeNewRandomUserClient(client);
	UserClientRole userClientRole = makeNewRandomUserClientRole(client);
	UserClientUserClientRole entity = new UserClientUserClientRole();
	entity.setUserClient(userClient);
	entity.setUserClientRole(userClientRole);
	userClientUserClientRoleService.create(entity);

	UserClientUserClientRole reloadNull = userClientUserClientRoleService
		.reload(null);
	assertThat("Should return null.", reloadNull == null, is(true));

	UserClientUserClientRole reload = userClientUserClientRoleService
		.reload(entity);
	assertThat("Should return existing UserClientUserClientRole",
		reload.getId() == entity.getId(), is(true));

	UserClientUserClientRole reloadA = userClientUserClientRoleService
		.reload(new UserClientUserClientRole());
	assertThat("Should return null", reloadA == null, is(true));
    }

    @Test
    public void testDelete() {
	Client client = makeNewRandomClient();
	UserClient userClient = makeNewRandomUserClient(client);
	UserClientRole userClientRole = makeNewRandomUserClientRole(client);
	UserClientUserClientRole entity = new UserClientUserClientRole();
	entity.setUserClient(userClient);
	entity.setUserClientRole(userClientRole);
	userClientUserClientRoleService.create(entity);

	userClientUserClientRoleService.delete(new UserClientUserClientRole());
	userClientUserClientRoleService.delete(entity);
	UserClientUserClientRole reloadAfterDelete = userClientUserClientRoleService
		.reload(entity);
	assertThat("should return nothing", reloadAfterDelete == null, is(true));
    }
}
