package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientTeamRolePersistence;

/**
 * Integration test for UserClientPersistence
 */
public class UserClientUserClientTeamRolePersistenceIntegrationTest extends
	BaseIntegrationTest {

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientUserClientTeamRolePersistence userClientUserClientTeamRolePersistence;

    @Test
    public void testCreate() {
	Client client = makeNewRandomClient();
	Team team = makeNewRandomTeam();
	UserClient userClient = makeNewRandomUserClient(client);
	UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);

	UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
	entity.setUserClient(userClient);
	entity.setTeam(team);
	entity.setUserClientTeamRole(userClientTeamRole);

	UserClientUserClientTeamRole created = userClientUserClientTeamRolePersistence
		.saveOrUpdate(entity);
	assertThat("entity created", created.getId(), is(notNullValue()));
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
	userClientUserClientTeamRolePersistence.saveOrUpdate(entity);

	Page<UserClientUserClientTeamRole> pagedResourceWithPageable = userClientUserClientTeamRolePersistence
		.findByUserClient(userClient, new PageRequest(0, 10));
	assertThat("should find at least one UserClientUserClientTeamRole",
		pagedResourceWithPageable.hasContent(), is(true));

	Page<UserClientUserClientTeamRole> pagedResource = userClientUserClientTeamRolePersistence
		.findByUserClient(userClient, null);
	assertThat("should find at least one UserClientUserClientTeamRole",
		pagedResource.hasContent(), is(true));
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
	userClientUserClientTeamRolePersistence.saveOrUpdate(entity);

	UserClientUserClientTeamRole reloadNull = userClientUserClientTeamRolePersistence
		.reload(null);
	assertThat("should return null", reloadNull == null, is(true));

	UserClientUserClientTeamRole reload = userClientUserClientTeamRolePersistence
		.reload(entity.getId());
	assertThat("should return the existing one",
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
	userClientUserClientTeamRolePersistence.saveOrUpdate(entity);

	userClientUserClientTeamRolePersistence.delete(entity.getId());
	UserClientUserClientTeamRole reloadAfterDelete = userClientUserClientTeamRolePersistence
		.reload(entity.getId());
	assertThat("should return nothing", reloadAfterDelete == null, is(true));
    }

}
