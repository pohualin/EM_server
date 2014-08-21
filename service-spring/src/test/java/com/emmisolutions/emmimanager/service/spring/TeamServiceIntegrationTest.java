package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.TeamService;

/**
 * Team Service Integration test
 */
public class TeamServiceIntegrationTest extends BaseIntegrationTest {
	
	@Resource
	ClientService clientService;
	
	@Resource
	TeamService teamService;
	
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
    	Client client = makeClient("toCreate", "teamUser");
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
}
