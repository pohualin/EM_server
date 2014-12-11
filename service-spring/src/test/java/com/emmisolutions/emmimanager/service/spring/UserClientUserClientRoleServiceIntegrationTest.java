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
		.findByUserClient(userClient.getId(), null);
	assertThat("Should return a page of UserClientUserClientRole.",
		ucucr.hasContent(), is(true));

	Page<UserClientUserClientRole> ucucrA = userClientUserClientRoleService
		.findByUserClient(userClient.getId(), new PageRequest(0, 10));
	assertThat("Should return a page of UserClientUserClientRole.",
		ucucrA.hasContent(), is(true));
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
		.reload(entity.getId());
	assertThat("Should return existing UserClientUserClientRole",
		reload.getId() == entity.getId(), is(true));
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

	userClientUserClientRoleService.delete(entity.getId());
	UserClientUserClientRole reloadAfterDelete = userClientUserClientRoleService
		.reload(entity.getId());
	assertThat("should return nothing", reloadAfterDelete == null, is(true));
    }
}
