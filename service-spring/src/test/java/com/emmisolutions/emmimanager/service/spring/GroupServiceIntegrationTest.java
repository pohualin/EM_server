package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.GroupSearchFilter;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;

public class GroupServiceIntegrationTest extends BaseIntegrationTest {
	
	@Resource
    ClientService clientService;

    @Resource
    UserPersistence userPersistence;
    
	@Resource
    GroupService groupService;
	
	private User superAdmin;

	@Before
	public void init() {
	    superAdmin = userPersistence.reload("super_admin");
	}
	
	@Test
	public void createGroupWithClientID(){
		Group group = new Group();
		group.setName("New Group");
		Long clientId = 1L;
		groupService.create(group, clientId);
		assertThat("Group associated to ClientId was created:", group.getClient().getId(),  is(notNullValue()));
	}
	
	@Test
	public void testListGroupsByClientId(){

		Group groupOne = new Group();
		groupOne.setName("TestGroup1");
		Client clientOne = makeClient();
		clientService.create(clientOne);
		groupOne.setClient(clientService.reload(clientOne));
		groupOne = groupService.save(groupOne);

		Group groupTwo = new Group();
		groupTwo.setName("TestGroup2");
		groupTwo.setClient(clientService.reload(clientOne));
		groupTwo = groupService.save(groupTwo);
		
		GroupSearchFilter gsf = new GroupSearchFilter(clientOne.getId());
		Page<Group> groupPage = groupService.list(null, gsf);
		
		assertThat("found Page of groups for clientId:", groupPage.getTotalElements(), is(2l));
		assertThat("we are on page 0", groupPage.getNumber(), is(0));
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
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
	    return client;
	}
	
	@Test
	public void testGroupNameUpdate(){
		Group groupA = new Group();
		groupA.setName("GroupNameA");
		Client clientOne = makeClient();
		clientService.create(clientOne);
		groupA.setClient(clientService.reload(clientOne));
		Group savedGroup = groupService.save(groupA);
		assertThat("original name is: ", savedGroup.getName(), is("GroupNameA"));
		savedGroup.setName("GroupNameB");
		Group updatedGroup = groupService.update(savedGroup);
		assertThat("Group Name is updated: ", updatedGroup.getName(), is("GroupNameB"));
		assertThat("Update was made on the same group ID: ", savedGroup.getId(), is(updatedGroup.getId()));
	}
	
	@Test
	public void testGroupById(){
		Group group = new Group();
		group.setName("GroupNameA");
		Client clientOne = makeClient();
		clientService.create(clientOne);
		group.setClient(clientService.reload(clientOne));
		Group savedGroup = groupService.save(group);
		Group retrievedGroup = groupService.reload(savedGroup.getId());
		assertThat("Group get by ID successfully retrieved group:", retrievedGroup.getId(), is(savedGroup.getId()));
	}
}
