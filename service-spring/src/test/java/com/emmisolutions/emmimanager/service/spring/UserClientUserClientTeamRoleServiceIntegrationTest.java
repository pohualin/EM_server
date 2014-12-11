package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientUserClientTeamRoleService;

/**
 * An integration test that goes across a wired persistence layer as well
 */
public class UserClientUserClientTeamRoleServiceIntegrationTest extends
	BaseIntegrationTest {

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientUserClientTeamRoleService userClientUserClientTeamRoleService;

    /**
     * Create with required values
     */
    @Test
    public void testUserClientUserClientTeamRoleCreate() {
	Client client = makeNewRandomClient();
	Team team = makeNewRandomTeam();
	UserClient userClient = makeNewRandomUserClient(client);
	UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);

	UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
	entity.setUserClient(userClient);
	entity.setTeam(team);
	entity.setUserClientTeamRole(userClientTeamRole);
	userClientUserClientTeamRoleService.create(entity);
    }

    @Test
    public void testFindByUserClient() {
	Client client = makeNewRandomClient();
	Team team = makeNewRandomTeam();
	UserClient userClient = makeNewRandomUserClient(client);
	UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
	UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
	entity.setUserClient(userClient);
	entity.setTeam(team);
	entity.setUserClientTeamRole(userClientTeamRole);
	userClientUserClientTeamRoleService.create(entity);

	Page<UserClientUserClientTeamRole> ucucr = userClientUserClientTeamRoleService
		.findByUserClient(userClient.getId(), null);
	assertThat("Should return a page of UserClientUserClientTeamRole.",
		ucucr.hasContent(), is(true));

	Page<UserClientUserClientTeamRole> ucucrA = userClientUserClientTeamRoleService
		.findByUserClient(userClient.getId(), new PageRequest(0, 10));
	assertThat("Should return a page of UserClientUserClientTeamRole.",
		ucucrA.hasContent(), is(true));
    }

    @Test
    public void testReload() {
	Client client = makeNewRandomClient();
	Team team = makeNewRandomTeam();
	UserClient userClient = makeNewRandomUserClient(client);
	UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
	UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
	entity.setUserClient(userClient);
	entity.setTeam(team);
	entity.setUserClientTeamRole(userClientTeamRole);
	userClientUserClientTeamRoleService.create(entity);

	UserClientUserClientTeamRole reloadNull = userClientUserClientTeamRoleService
		.reload(null);
	assertThat("Should return null.", reloadNull == null, is(true));

	UserClientUserClientTeamRole reload = userClientUserClientTeamRoleService
		.reload(entity.getId());
	assertThat("Should return existing UserClientUserClientTeamRole",
		reload.getId() == entity.getId(), is(true));
    }

    @Test
    public void testDelete() {
	Client client = makeNewRandomClient();
	Team team = makeNewRandomTeam();
	UserClient userClient = makeNewRandomUserClient(client);
	UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
	UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
	entity.setUserClient(userClient);
	entity.setTeam(team);
	entity.setUserClientTeamRole(userClientTeamRole);
	userClientUserClientTeamRoleService.create(entity);

	userClientUserClientTeamRoleService.delete(entity.getId());
	UserClientUserClientTeamRole reloadAfterDelete = userClientUserClientTeamRoleService
		.reload(entity.getId());
	assertThat("should return nothing", reloadAfterDelete == null, is(true));
    }
}
