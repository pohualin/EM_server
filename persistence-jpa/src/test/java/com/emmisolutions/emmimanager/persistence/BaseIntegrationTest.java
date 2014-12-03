package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.configuration.PersistenceConfiguration;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Root integration test harness
 */
@ContextConfiguration(classes = PersistenceConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
@Transactional
public abstract class BaseIntegrationTest {

    @Resource
    UserAdminRepository userAdminRepository;

    @Resource
    ClientPersistence clientPersistence;

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
        return clientPersistence.save(client);
    }
    
    protected UserAdmin makeNewRandomUserAdmin() {
		UserAdmin userAdmin = new UserAdmin(RandomStringUtils
				.randomAlphabetic(255), RandomStringUtils
				.randomAlphanumeric(100));
		userAdmin.setFirstName(RandomStringUtils.randomAlphabetic(10));
		userAdmin.setLastName(RandomStringUtils.randomAlphabetic(10));
		return userAdminRepository.save(userAdmin);
	}

}
