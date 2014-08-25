package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

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
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.GroupPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientRepository;
import com.emmisolutions.emmimanager.persistence.repo.GroupRepository;

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
	
	private User superAdmin;

	@Before
	public void init() {
	    superAdmin = userPersistence.reload("super_admin");
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
	public void testGroupWithTags() {

		Group group = new Group();
		group.setName("new group");
		
		Client client = makeClient();
		clientRepository.save(client);
	
		group.setClient(clientRepository.findOne(client.getId()));
		
		Tag tagOne = new Tag();
		tagOne.setName("new tag one");
		tagOne.setGroup(group);
		Tag tagTwo = new Tag();
		tagTwo.setName("new tag two");
		tagTwo.setGroup(group);
		Set<Tag> tags = new HashSet<Tag>();
		tags.add(tagOne);
		tags.add(tagTwo);

		group.setTags(tags);
		group = groupRepository.save(group);
		
		assertThat("Group with tags was created: ", group.getTags().iterator().next().getId(), is(notNullValue()));
		assertThat("Group with 2 tags was created ", group.getTags().size(), is(2));
	}
	
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
	}
	
	@Test(expected=NullPointerException.class)
	public void deleteGroupById(){
		Group groupToDelete = new Group();
		groupToDelete.setName("TestGroupToDelete");
		Client clientOne = makeClient();
		clientPersistence.save(clientOne);
		groupToDelete.setClient(clientPersistence.reload(clientOne.getId()));
		groupToDelete = groupPersistence.save(groupToDelete);
		assertThat("Group is created:", groupToDelete.getId(), is(notNullValue()));
		
		groupPersistence.remove(groupToDelete.getId());
		Group group= groupPersistence.reload(groupToDelete.getId());
		assertThat("Group was removed and no longer exists", group.getId(), is(9l));

	}
}