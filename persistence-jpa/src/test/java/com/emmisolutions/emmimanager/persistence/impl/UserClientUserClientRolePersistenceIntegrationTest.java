package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientRolePersistence;

/**
 * Integration test for UserClientPersistence
 */
public class UserClientUserClientRolePersistenceIntegrationTest extends
	BaseIntegrationTest {

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientUserClientRolePersistence userClientUserClientRolePersistence;

    @Test
    public void testCreate() {
	Client client = makeNewRandomClient();
	UserClient userClient = makeUserClient(client);
	UserClientRole userClientRole = makeNewRandomUserClientRole(client);

	UserClientUserClientRole entity = new UserClientUserClientRole();
	entity.setUserClient(userClient);
	entity.setUserClientRole(userClientRole);

	UserClientUserClientRole created = userClientUserClientRolePersistence
		.saveOrUpdate(entity);
	assertThat("entity created", created.getId(), is(notNullValue()));
    }

    @Test
    public void testFindByUserClient() {
	Client client = makeNewRandomClient();
	UserClient userClient = makeUserClient(client);
	UserClientRole userClientRole = makeNewRandomUserClientRole(client);
	UserClientUserClientRole entity = new UserClientUserClientRole();
	entity.setUserClient(userClient);
	entity.setUserClientRole(userClientRole);
	userClientUserClientRolePersistence.saveOrUpdate(entity);

	Page<UserClientUserClientRole> pagedResourceWithPageable = userClientUserClientRolePersistence
		.findByUserClient(userClient, new PageRequest(0, 10));
	assertThat("should find at least one UserClientUserClientRole",
		pagedResourceWithPageable.hasContent(), is(true));

	Page<UserClientUserClientRole> pagedResource = userClientUserClientRolePersistence
		.findByUserClient(userClient, null);
	assertThat("should find at least one UserClientUserClientRole",
		pagedResource.hasContent(), is(true));
    }

    private UserClient makeUserClient(Client client) {
	UserClient userClient = new UserClient();
	userClient.setClient(client);
	userClient.setFirstName(RandomStringUtils.randomAlphabetic(10));
	userClient.setLastName(RandomStringUtils.randomAlphabetic(10));
	userClient.setLogin(RandomStringUtils.randomAlphabetic(10));
	return userClientPersistence.saveOrUpdate(userClient);
    }
}
