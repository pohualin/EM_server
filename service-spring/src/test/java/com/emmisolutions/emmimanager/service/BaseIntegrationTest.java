package com.emmisolutions.emmimanager.service;

import javax.annotation.Resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.service.configuration.ServiceConfiguration;

import org.joda.time.LocalDate;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

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
	 UserService userService;
	 
	protected Client makeClient(String clientName, String username){
        Client client = new Client();
        client.setType(ClientType.PROVIDER);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        client.setContractOwner(userService.save(new User(username, "pw")));
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        return client;
    }
}
