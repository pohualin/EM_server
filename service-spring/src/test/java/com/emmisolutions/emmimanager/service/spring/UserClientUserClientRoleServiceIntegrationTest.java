package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.junit.Test;

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
	UserClientRole userClientRole = makeNewRandomUserClientRole(client);

	// TODO replace this block
	UserClient userClient = new UserClient();
	userClient.setClient(client);
	userClient.setFirstName("dfadfkjpoew");
	userClient.setLastName("iewoprulkxc");
	userClient.setLogin("piewueiokvlx");
	userClient = userClientService.create(userClient);

	UserClientUserClientRole entity = new UserClientUserClientRole();
	entity.setUserClient(userClient);
	entity.setUserClientRole(userClientRole);
	userClientUserClientRoleService.create(entity);
    }

}
