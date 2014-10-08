package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientRepository;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.GroupRepository;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Group Persistence Integration Test
 *
 */
public class GroupPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientPersistence clientPersistence;
    
    @Resource
    GroupPersistence groupPersistence;
    
	@Resource
	UserPersistence userPersistence;
	
	@Resource
	GroupRepository groupRepository;
	
	@Resource
	ClientRepository clientRepository;

    @Resource
    ClientTypeRepository clientTypeRepository;

	User superAdmin;

    ClientType clientType;

	@Before
	public void init() {
	    superAdmin = userPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
	}
	
	private Client makeClient(){
	    Client client = new Client();
	    client.setTier(new ClientTier(3l));
	    client.setContractEnd(LocalDate.now().plusYears(1));
	    client.setContractStart(LocalDate.now());
	    client.setRegion(new ClientRegion(1l));
	    client.setName("Test Client");
	    client.setType(clientType);
	    client.setActive(false);
	    client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
	    return client;
	}
	
	/**
	 * 	Test list of groups by client id
	 */
	@Test
	public void testListGroupsByClientID(){
		Group groupOne = new Group();
		groupOne.setName("TestGroup1");
		Client clientOne = makeClient();
		clientPersistence.save(clientOne);
		groupOne.setClient(clientPersistence.reload(clientOne.getId()));
		groupOne = groupPersistence.save(groupOne);

		Group groupTwo = new Group();
		groupTwo.setName("TestGroup2");
		groupTwo.setClient(clientPersistence.reload(clientOne.getId()));
		groupTwo = groupPersistence.save(groupTwo);
		
		GroupSearchFilter gsf = new GroupSearchFilter(clientOne.getId());
		Page<Group> groupPage = groupPersistence.list(null, gsf);
		
		assertThat("found all of the clients", groupPage.getTotalElements(), is(2l));
		assertThat("we are on page 0", groupPage.getNumber(), is(0));
		assertThat("we are on page 0", groupPage.getContent().iterator().next().getClient().getId(), is(clientOne.getId()));

	}
}