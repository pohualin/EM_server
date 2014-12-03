package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.configuration.ServiceConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.annotation.Resource;

/**
 * Root integration test harness
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
//@Transactional - do not enable this.. the service implementation should be annotated correctly!
public abstract class BaseIntegrationTest {

    @Resource
    ClientService clientService;

    @Resource
    UserService userService;

    /**
     * Creates a brand new client that shouldn't already be inserted
     *
     * @return random client
     */
    protected Client makeNewRandomClient() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName(RandomStringUtils.randomAlphanumeric(255));
        client.setType(new ClientType(1l));
        client.setActive(true);
        client.setContractOwner(makeNewRandomUserAdmin());
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return clientService.create(client);
    }
    
	protected UserAdmin makeNewRandomUserAdmin() {
		UserAdmin userAdmin = new UserAdmin(RandomStringUtils
				.randomAlphabetic(255), RandomStringUtils
				.randomAlphanumeric(100));
		userAdmin.setFirstName(RandomStringUtils.randomAlphabetic(10));
		userAdmin.setLastName(RandomStringUtils.randomAlphabetic(10));
		return userService.save(userAdmin);
	}
}
