package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientRolePersistence;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Integration test for UserClientPersistence
 */
public class UserClientUserClientRolePersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientUserClientRolePersistence userClientUserClientRolePersistence;

    /**
     * Test create an UserClientUserClientRole
     */
    @Test
    public void testCreate() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientRole userClientRole = makeNewRandomUserClientRole(client);

        UserClientUserClientRole entity = new UserClientUserClientRole();
        entity.setUserClient(userClient);
        entity.setUserClientRole(userClientRole);

        UserClientUserClientRole created = userClientUserClientRolePersistence
                .saveOrUpdate(entity);
        assertThat("entity created", created.getId(), is(notNullValue()));
    }

    /**
     * Test find all existing UserClientUserClientRole by userClientId
     */
    @Test
    public void testFindByUserClient() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
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

    /**
     * Test reload an UserClientUserClientRole by given
     * userClientUserClientRoleId
     */
    @Test
    public void testReload() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientRole userClientRole = makeNewRandomUserClientRole(client);
        UserClientUserClientRole entity = new UserClientUserClientRole();
        entity.setUserClient(userClient);
        entity.setUserClientRole(userClientRole);
        userClientUserClientRolePersistence.saveOrUpdate(entity);

        UserClientUserClientRole reloadNull = userClientUserClientRolePersistence
                .reload(null);
        assertThat("should return null", reloadNull == null, is(true));

        UserClientUserClientRole reload = userClientUserClientRolePersistence
                .reload(entity.getId());
        assertThat("should return the existing one",
                reload.getId() == entity.getId(), is(true));
    }

    /**
     * Test delete an UserClientUserClientRole by given
     * userClientUserClientRoleId
     */
    @Test
    public void testDelete() {
        Client client = makeNewRandomClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientRole userClientRole = makeNewRandomUserClientRole(client);
        UserClientUserClientRole entity = new UserClientUserClientRole();
        entity.setUserClient(userClient);
        entity.setUserClientRole(userClientRole);
        userClientUserClientRolePersistence.saveOrUpdate(entity);

        userClientUserClientRolePersistence.delete(entity.getId());
        UserClientUserClientRole reloadAfterDelete = userClientUserClientRolePersistence
                .reload(entity.getId());
        assertThat("should return nothing", reloadAfterDelete == null, is(true));
    }

}
