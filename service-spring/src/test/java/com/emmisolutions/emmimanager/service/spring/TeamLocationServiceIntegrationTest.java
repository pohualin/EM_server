package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.service.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
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
    ClientLocationPersistence clientLocationPersistence;

    @Resource
    ClientLocationService clientLocationService;

	@Resource
    UserService userService;

	@Resource
    LocationService locationService;

	/**
     * Create a Team associated to a new client, then add location to the team and those locations
     * should have to be associated to the client's team. Also hit the delete by client and location
     */
    @Test
    public void save() {
    	Client client = makeClient("TEST-CLIENT", "TEST-USER");
    	clientService.create(client);

    	Team team = makeTeamForClient(client, "1");
        Team savedTeam = teamService.create(team);
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));

        Set<TeamLocationTeamProviderSaveRequest> reqs = new HashSet<TeamLocationTeamProviderSaveRequest>();
        TeamLocationTeamProviderSaveRequest req = new TeamLocationTeamProviderSaveRequest();
  
        Location location = locationService.create( makeLocation("Location ", "1") );
        req.setLocation(location);
        reqs.add(req);

        assertThat("there is no client location yet", clientLocationPersistence.reload(location.getId(), client.getId()), is(nullValue()));
      
        // create the team location
        teamLocationService.save(savedTeam, reqs);

        assertThat("location also added to the client location", clientLocationPersistence.reload(location.getId(), client.getId()), is(notNullValue()));

        assertThat("team should be found when searching by client and location",
            teamLocationService.findTeamsBy(client, location, null), hasItem(savedTeam));

        assertThat("a client location should have also been created", clientLocationPersistence.reload(location.getId(), client.getId()).getLocation(), is(location));

        assertThat("should delete one team location", teamLocationService.delete(client, location), is(1l));

        assertThat("should find no teams",
            teamLocationService.findTeamsBy(client, location, null).getSize(), is(0));
    }

    /**
     * Test that a delete on a client location removes the team location as well.
     */
    @Test
    public void deleteOnClientDeletesTeam(){
        Client client = makeClient(RandomStringUtils.randomAlphabetic(255),
            RandomStringUtils.randomAlphabetic(50));
        clientService.create(client);

        Team team = makeTeamForClient(client, RandomStringUtils.randomNumeric(15));
        Team savedTeam = teamService.create(team);
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));

        Set<TeamLocationTeamProviderSaveRequest> reqs = new HashSet<TeamLocationTeamProviderSaveRequest>();
        TeamLocationTeamProviderSaveRequest req = new TeamLocationTeamProviderSaveRequest();

        Location location = locationService.create( makeLocation("Location ",  RandomStringUtils.randomNumeric(15)) );
        req.setLocation(location);
        reqs.add(req);

        // create the team location
        teamLocationService.save(savedTeam, reqs);

        Page<TeamLocation> teamLocationPage = teamLocationService.findAllTeamLocationsWithTeam(null, savedTeam);

        assertThat("team location was reloaded", teamLocationService.reload(teamLocationPage.iterator().next()).getLocation(), is(location));

        ClientLocation clientLocation = clientLocationPersistence.reload(location.getId(), client.getId());

        assertThat("client location should be there", clientLocation, is(notNullValue()));

        clientLocationService.remove(clientLocation);

        assertThat("team location should not reload anymore", teamLocationService.reload(teamLocationPage.iterator().next()), is(nullValue()));

    }

    /**
     * Test a delete after save
     */
    @Test
    public void delete() {
    	Client client = makeClient("OTRO mas TEST-CLIENT2", "TEST-USER2");
    	clientService.create(client);

    	Team team = makeTeamForClient(client, "1");
        Team savedTeam = teamService.create(team);
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));

        Set<TeamLocationTeamProviderSaveRequest> reqs = new HashSet<TeamLocationTeamProviderSaveRequest>();
        TeamLocationTeamProviderSaveRequest req = new TeamLocationTeamProviderSaveRequest();

        Location location = locationService.create( makeLocation("Location ", "1") );
        req.setLocation(location);
        reqs.add(req);
        
        teamLocationService.save(savedTeam, reqs);

        Page<TeamLocation> teamLocationPage = teamLocationService.findAllTeamLocationsWithTeam(null,team);
        for (TeamLocation teamLocation : teamLocationPage.getContent()) {
        	teamLocationService.delete(teamLocation);
		}

        assertThat("location was not removed from the client", clientLocationPersistence.reload(location.getId(), client.getId()), is(notNullValue()));

        teamLocationPage = teamLocationService.findAllTeamLocationsWithTeam(null,team);

        assertThat("team location was removed successfully", teamLocationPage.getContent().size(), is(0));

    }

    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void invalidFindByClientAndLocation(){
        teamLocationService.findTeamsBy(null, null, null);
    }

    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void invalidDeleteByClientAndLocation(){
        teamLocationService.delete(null, null);
    }

    private Team makeTeamForClient(Client client, String id){
		 Team team = new Team();
		 team.setName("Test Team " + id);
		 team.setDescription("Test Team description" + id);
		 team.setActive(false);
		 team.setClient(client);
		 team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
		 return team;
	 }

    private Client makeClient(String clientName, String username){
        Client client = new Client();
        client.setType(new ClientType(4l));
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        client.setContractOwner(userService.save(new UserAdmin(username, "pw")));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
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
