package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.UserService;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));
    }

    @Test
    public void update(){
        Client client = makeClient("clientTeam2", "teamUser1");
        clientService.create(client);

        Team team = makeTeamForClient(client);
        Team savedTeam = teamService.create(team);
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));

        savedTeam.setClient(clientService.create(makeClient("a different client", "teamUser2")));
        savedTeam = teamService.update(team);
        assertThat("client was not updated", savedTeam.getClient(), is(client));
    }

    private Team makeTeamForClient(Client client){
		 Team team = new Team();
		 team.setName("Test Team");
		 team.setDescription("Test Team description");
		 team.setActive(false);
		 team.setClient(client);
		 team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
		 return team;
	 }

    protected Client makeClient(String clientName, String username){
        Client client = new Client();
        client.setType(new ClientType(4l));
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        client.setContractOwner(userService.save(new UserAdmin(username, "pw")));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return client;
    }
}
