package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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
import com.emmisolutions.emmimanager.service.UserService;

public class GroupServiceIntegrationTest extends BaseIntegrationTest {
	
	@Resource
    ClientService clientService;

    @Resource
    UserService userService;

    @Resource
    UserPersistence userPersistence;
    
    @Test
    public void testCreateClientGroup() {
        Client client = clientService.create(makeClientWithGroup("testClient", "me"));
        assertThat("client with group was created successfully", client.getId(), is(notNullValue()));
        assertThat("group id is not null",client.getGroups().iterator().next().getId(), is(notNullValue()));
    }
    
    @Test
    public void testCreateClientWithMultipleGroups() {
    	Client client = makeClient("testClient", "me");
    	Group group1 = new Group("testGroup1", true);
    	Group group2 = new Group("testGroup2", true);
    	
    	client.addGroup(group1);
    	client.addGroup(group2);
    	Client savedClient = clientService.create(client);
    	
    	assertThat("Client with 2 groups was created",savedClient.getGroups().size(), equalTo(2));
    }
    
    
    private Client makeClientWithGroup(String clientName, String username){
        Client client = makeClient(clientName,username);
        Group group = new Group("testGroup", true);
        client.addGroup(group);
        return client;
    }
    
    private Client makeClient(String clientName, String username){
        Client client = new Client();
        client.setType(ClientType.PROVIDER);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        User newGuy = new User(username, "pw");
        client.setContractOwner(userService.save(newGuy));
        return client;
    }
}
