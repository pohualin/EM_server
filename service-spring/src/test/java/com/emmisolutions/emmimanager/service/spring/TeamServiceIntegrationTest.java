package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.UserService;

/**
 * Team Service Integration test
 */
public class TeamServiceIntegrationTest extends BaseIntegrationTest {
	
	@Resource
	ClientService clientService;
	
	@Resource
	TeamService teamService;
	
	@Resource
    UserService userService;
	
	/**
     * Not all required fields
     */
    @Test(expected = ConstraintViolationException.class)
    public void createNotAllRequired() {
        Team team = new Team();
        teamService.create(team);
    }
    
    /**
     * Create team successfully
     */
    @Test
    public void create() {
    	Client client = makeClient("clientTeam", "teamUser");
    	clientService.create(client);
    	
    	Team team = makeTeamForClient(client);
        Team savedTeam = teamService.create(team);
        assertThat("client was created successfully", savedTeam.getId(), is(notNullValue()));
    }
    
    private Team makeTeamForClient(Client client){
		 Team team = new Team();
		 team.setName("Test Team");
		 team.setDescription("Test Team description");
		 team.setActive(false);
		 team.setPhone("1111111111");
		 team.setFax("2222222222");
		 team.setClient(client);
		 return team;
	 }
    
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
