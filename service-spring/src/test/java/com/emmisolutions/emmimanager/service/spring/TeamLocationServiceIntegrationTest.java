package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.*;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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
    	Client client = makeClient("TEST-CLIENT", "TEST-USER");
    	clientService.create(client);
    	
    	Team team = makeTeamForClient(client, "1");
        Team savedTeam = teamService.create(team);
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));

        Set<Location> locationSet = new HashSet<Location>();
        Location loca = locationService.create( makeLocation("Location ", "1") );
        locationSet.add(loca);
        teamLocationService.save(savedTeam, locationSet);

        //@TODO: Fix this test to deal with client location
//        assertThat("location also added to the client location", locationService.reloadLocationUsedByClient(client, loca), is(notNullValue()));
        
    }
    
    @Test
    public void delete() {
    	Client client = makeClient("OTRO mas TEST-CLIENT2", "TEST-USER2");
    	clientService.create(client);
    	
    	Team team = makeTeamForClient(client, "1");
        Team savedTeam = teamService.create(team);
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));

        Set<Location> locationSet = new HashSet<Location>();
        Location loca = locationService.create( makeLocation("Location ", "1") );
        locationSet.add(loca);
        teamLocationService.save(savedTeam, locationSet);
        
        Page<TeamLocation> teamLocationPage = teamLocationService.findAllTeamLocationsWithTeam(null,team);
        for (TeamLocation teamLocation : teamLocationPage.getContent()) {
        	teamLocationService.delete(teamLocation);
		}

        //@TODO: Fix this for client location
//        assertThat("location was not removed from the client", locationService.reloadLocationUsedByClient(client, loca), is(notNullValue()));
       
        teamLocationPage = teamLocationService.findAllTeamLocationsWithTeam(null,team);

        assertThat("team location was removed successfully", teamLocationPage.getContent().size(), is(0));

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
