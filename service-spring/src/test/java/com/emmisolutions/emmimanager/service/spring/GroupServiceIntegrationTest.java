package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;
import com.emmisolutions.emmimanager.service.UserService;

public class GroupServiceIntegrationTest extends BaseIntegrationTest {
	
	@Resource
    ClientService clientService;

    @Resource
    UserService userService;

    @Resource
    UserPersistence userPersistence;
    
	@Resource
    GroupService groupService;
    
	@Test
	public void testCreateClientWithMultipleGroups() {

		Client client = new Client();
		client.setType(ClientType.PROVIDER);
		client.setContractStart(LocalDate.now());
		client.setContractEnd(LocalDate.now().plusYears(1));
		client.setName("test Client");
		User newGuy = new User("me1", "pw");
		client.setContractOwner(userService.save(newGuy));

		Client savedClient = clientService.create(client);

		Group group1 = new Group("testGroup1", savedClient);
		Group group2 = new Group("testGroup2", savedClient);

		Group groupOne = groupService.save(group1);
		Group groupTwo = groupService.save(group2);

		Set<Group> groups = new HashSet<Group>();
		groups.add(groupOne);
		groups.add(groupTwo);

		savedClient.setGroups(groups);

		assertThat("Client with 2 groups was created", savedClient.getGroups().size(), equalTo(2));
    }
	
	@Test
	public void testCreateClientWithGroups() {

		Client client = new Client();
		client.setType(ClientType.PROVIDER);
		client.setContractStart(LocalDate.now());
		client.setContractEnd(LocalDate.now().plusYears(1));
		client.setName("test Client");
		User newGuy = new User("me5", "pw");
		client.setContractOwner(userService.save(newGuy));

		Client savedClient = clientService.create(client);

		Group group1 = new Group("testGroup1", savedClient);
		Group group2 = new Group("testGroup2", savedClient);
		Group group3 = new Group("testGroup3", savedClient);

		List<Group> listOfGroups = new ArrayList<Group>();
		listOfGroups.add(group1);
		listOfGroups.add(group2);
		listOfGroups.add(group3);

		List<Group> savedGroups = new ArrayList<Group>();

		for (Group group : listOfGroups) {
			Group savedGroup = groupService.save(group);
			savedGroups.add(savedGroup);
		}

		if (savedGroups != null && !savedGroups.isEmpty()) {
			Set<Group> groups1 = new HashSet<Group>(savedGroups);
			savedClient.setGroups(groups1);
		}
		assertThat("Client with 3 groups was created", savedClient.getGroups().size(), equalTo(3));
    }
}
