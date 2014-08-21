package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.configuration.PersistenceConfiguration;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Root integration test harness
 */
@ContextConfiguration(classes = PersistenceConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
@Transactional
public abstract class BaseIntegrationTest {

    @PersistenceContext(unitName = "EmmiManagerPersistenceUnit")
    protected EntityManager entityManager;
    
    protected User superAdmin;
    
    @Resource
    UserPersistence userPersistence;
    
    /**
     * Before each test
     */
    @Before
    public void init() {
        superAdmin = userPersistence.reload("super_admin");
    }
    
    protected Client makeClient(){
    	Client client = new Client();
        client.setTier(ClientTier.THREE);
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(ClientRegion.NORTHEAST);
        client.setName("Test Client");
        client.setType(ClientType.PROVIDER);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        return client;
    }
}
