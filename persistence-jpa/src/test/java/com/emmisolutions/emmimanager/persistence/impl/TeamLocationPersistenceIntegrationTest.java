package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.State;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;

public class TeamLocationPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    TeamLocationPersistence teamLocationPersistence;

    @Resource
    UserPersistence userPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    User superAdmin;

    @Resource
    ClientTypeRepository clientTypeRepository;

    ClientType clientType;

    @Before
    public void init() {
        superAdmin = userPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
    }

    /**
     * Save success
     */
    @Test
    public void save() {
        TeamLocation teamLocation = new TeamLocation();

        Client client = createClient("1");
        Location location = createLocation(client,"1");
        Team team = createTeam(client, 1);

        teamLocation.setLocation(location);
        teamLocation.setTeam(team);

        TeamLocation afterSaveTeamLocation = teamLocationPersistence.saveTeamLocation(teamLocation);
        assertThat("TeamLocation was given an id", afterSaveTeamLocation.getId(), is(notNullValue()));
        assertThat("system is the created by", afterSaveTeamLocation.getCreatedBy(), is("system"));
    }

    /*
     * try to save a null team location
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveNullTeamLocation() {
    	teamLocationPersistence.saveTeamLocation(null);
    }


    /**
     * Test getting all the TeamLocations for a given team
     */
    @Test
    public void getAllTeamLocationForTeam() {
    	TeamLocation teamLocation1 = new TeamLocation();
    	TeamLocation teamLocation2 = new TeamLocation();
    	TeamLocation teamLocation3 = new TeamLocation();

        Client client = createClient("1");

        Location location1 = createLocation(client,"1");
        Location location2 = createLocation(client,"2");

        Team team1 = createTeam(client, 1);
        Team team3 = createTeam(client, 3);

        teamLocation1.setLocation(location1);
        teamLocation1.setTeam(team1);
        teamLocation2.setLocation(location2);
        teamLocation2.setTeam(team1);
        teamLocation3.setLocation(location1);
        teamLocation3.setTeam(team3);


        teamLocationPersistence.saveTeamLocation(teamLocation1);
        teamLocationPersistence.saveTeamLocation(teamLocation2);
        teamLocationPersistence.saveTeamLocation(teamLocation3);
        assertThat("TeamLocation was given an id", teamLocation1.getId(), is(notNullValue()));
        assertThat("TeamLocation was given an id", teamLocation2.getId(), is(notNullValue()));
        assertThat("TeamLocation was given an id", teamLocation3.getId(), is(notNullValue()));

        Page<TeamLocation> associatedTeamLocationPage = teamLocationPersistence.getAllTeamLocationsForTeam(null,team1);
        assertThat("2 teams were correctly returned", associatedTeamLocationPage.getTotalElements(), is(2L));

        associatedTeamLocationPage = teamLocationPersistence.getAllTeamLocationsForTeam(null,team3);
        assertThat("1 team were correctly returned", associatedTeamLocationPage.getTotalElements(), is(1L));

    }

    private Team createTeam(Client client, int i) {
        Team team = new Team();
        team.setName("Test Team"+i);
        team.setDescription("Test Team description");
        team.setActive(i % 2 == 0);
        team.setClient(client);
        team.setSalesForceAccount(new TeamSalesForce("xxxWW" + System.currentTimeMillis()));
        team = teamPersistence.save(team);
        return team;
    }

    private Location createLocation(Client client, String uniqueId ) {
        Location location = new Location();
        location.setName("Test Location "+ uniqueId);
        location.setActive(true);
        location.setBelongsTo(client);
        location.setCity("CHICAGO");
        location.setPhone("630-222-8900");
        location.setState(State.IL);
        location.addClientUsingThisLocation(client);
        locationPersistence.save(location);
        return location;
    }

    private Client createClient(String uniqueId) {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("Test Client "+uniqueId);
        client.setType(clientType);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        clientPersistence.save(client);
        return client;
    }

}
