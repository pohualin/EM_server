package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.State;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.LocationService;
import com.emmisolutions.emmimanager.service.TeamLocationService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.UserService;

/**
 * Team Location Service Integration test
 */
public class TeamLocationServiceIntegrationTest extends BaseIntegrationTest {
	
	@Resource
	ClientService clientService;
	
	@Resource
	TeamService teamService;

	@Resource
	TeamLocationService teamLocationService;
	
	@Resource
    UserService userService;
    
	@Resource
    LocationService locationService;
    
	/**
     * Create a Team associated to a new client, then add location to the team and those locations should have to be associated to the client's team
     */
    @Test
    public void save() {
    	Client client = makeClient("clientTeam", "teamUser");
    	clientService.create(client);
    	
    	Team team = makeTeamForClient(client, "1");
        Team savedTeam = teamService.create(team);
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));

        Set<Location> locationSet = new HashSet<Location>();
        Location loca = locationService.create( makeLocation("Location ", "1") );
        locationSet.add(loca);
        teamLocationService.save(savedTeam, locationSet);
        
        assertThat("team location also asociated to the client's team successfully", locationService.reloadLocationUsedByClient(client, loca), is(notNullValue()));
        
    }
    
    private Team makeTeamForClient(Client client, String id){
		 Team team = new Team();
		 team.setName("Test Team " + id);
		 team.setDescription("Test Team description" + id);
		 team.setActive(false);
		 team.setClient(client);
		 team.setSalesForceAccount(new TeamSalesForce("xxxWW" + System.currentTimeMillis()));
		 return team;
	 }
    
    private Client makeClient(String clientName, String username){
        Client client = new Client();
        client.setType(new ClientType(4l));
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        client.setContractOwner(userService.save(new User(username, "pw")));
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        return client;
    }
    
    private Location makeLocation(String name, String i) {
        Location location = new Location();
        location.setName(name + i);
        location.setCity("Valid City " + i);
        location.setActive(true);
        location.setPhone("555-422-1212");
        location.setState(State.IL);
        return location;
    }
}
