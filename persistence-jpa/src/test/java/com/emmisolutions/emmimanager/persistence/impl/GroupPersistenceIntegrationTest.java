package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientRepository;
import com.emmisolutions.emmimanager.persistence.repo.GroupRepository;

public class GroupPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientPersistence clientPersistence;
	
	@Resource
	UserPersistence userPersistence;
	
	@Resource
	GroupRepository groupRepository;
	
	@Resource
	ClientRepository clientRepository;
	
	private User superAdmin;

	@Before
	public void init() {
	    superAdmin = userPersistence.reload("super_admin");
	}

	@Test
	public void testSaveClientWithAGroup() {
		Client client = makeClient();
	    client.addGroup(new Group("Test Group", true));
	    client = clientPersistence.save(client);
	    assertThat("Groups are not null", client.getGroups(), is(notNullValue()));
	    assertThat("Group has an id", client.getGroups().iterator().next().getId(), is(notNullValue()));
	 }
	
	@Test
	public void testGroupBelongsToClient() {
		Client client = makeClient();
	    client.addGroup(new Group("Test Group", true));
	    client = clientPersistence.save(client);
	    
	    Group retreivedGroup = groupRepository.findOne(client.getGroups().iterator().next().getId());	    
	    assertThat("Group saved is the same as the group retreived",client.getGroups().iterator().next(), is(retreivedGroup));
	}
	
	@Test
    public void testBelongsToRelationship(){
		Client client = makeClient();
		client.addGroup(new Group("Test Group", true));
	    client = clientPersistence.save(client);
	    
	    Group retreivedGroup = groupRepository.findOne(client.getGroups().iterator().next().getId());
	    assertThat("Client in the group is the same as the client that was saved",retreivedGroup.getClient(), is(client));
	}
	
	@Test
    public void testDuplicateActiveGroup(){
		Client client = makeClient();
		client.addGroup(new Group("Test Group", true));
		client.addGroup(new Group("Test Group", true));		
		client = clientPersistence.save(client);
		
		client = clientRepository.findOne(client.getId());
		assertThat("Duplicate groups are not stored",client.getGroups().size(), equalTo(1));
	}
	
	private Client makeClient(){
	    Client client = new Client();
	    client.setTier(ClientTier.THREE);
	    client.setContractEnd(LocalDate.now().plusYears(1));
	    client.setContractStart(LocalDate.now());
	    client.setRegion(ClientRegion.NORTHEAST);
	    client.setName("Test Client");
	    client.setType(ClientType.PROVIDER);
	    client.setActive(false);
	    client.setContractOwner(superAdmin);
	    return client;
	}
}
