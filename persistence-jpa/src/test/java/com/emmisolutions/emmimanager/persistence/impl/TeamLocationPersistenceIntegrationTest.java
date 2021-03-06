package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.*;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class TeamLocationPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    TeamLocationPersistence teamLocationPersistence;

    @Resource
    UserAdminPersistence userAdminPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    UserAdmin superAdmin;

    @Resource
    ClientTypeRepository clientTypeRepository;

    ClientType clientType;

    @Before
    public void init() {
        superAdmin = userAdminPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
    }

    /**
     * Save success
     */
    @Test
    public void save() {
        TeamLocation teamLocation = new TeamLocation();

        Client client = createClient("1");
        Location location = createLocation(client, "1");
        Team team = createTeam(client, 1);

        teamLocation.setLocation(location);
        teamLocation.setTeam(team);

        TeamLocation afterSaveTeamLocation = teamLocationPersistence.saveTeamLocation(teamLocation);
        assertThat("TeamLocation was given an id", afterSaveTeamLocation.getId(), is(notNullValue()));
        assertThat("found TeamLocation with team id and location id",
                teamLocationPersistence.findByTeamAndLocation(team.getId(),
                        location.getId()), is(teamLocation));
        
        try {
            teamLocationPersistence.findByTeamAndLocation(null,
                    location.getId());
            fail("Should fail when team id is null but it is not");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        try {
            teamLocationPersistence.findByTeamAndLocation(team.getId(), null);
            fail("Should fail when provider id is null but it is not");
        } catch (InvalidDataAccessApiUsageException e) {
        }

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

        Location location1 = createLocation(client, "1");
        Location location2 = createLocation(client, "2");

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

        assertThat("2 team locations were correctly returned", teamLocationPersistence.getAllTeamLocationsForTeam(null, team1),
                hasItems(teamLocation1, teamLocation2));

        assertThat("1 team location was correctly returned", teamLocationPersistence.getAllTeamLocationsForTeam(null, team3), hasItem(teamLocation3));

        assertThat("teams can be found by client and location",
                teamLocationPersistence.findTeamsBy(client, location1, null),
                hasItems(team1, team3));

        assertThat("team locations can be removed by client and location",
                teamLocationPersistence.delete(client, location1),
                is(2l));

        assertThat("teams should not be be found by client and location",
                teamLocationPersistence.findTeamsBy(client, location1, null),
                not(hasItems(team1, team3)));


    }

    /**
     * Delete a location team
     */
    @Test
    public void delete() {

        Client client = createClient("1");
        Location location = createLocation(client, "1");
        Team team = createTeam(client, 1);
        TeamLocation teamLocation = new TeamLocation(location, team);

        TeamLocation afterSaveTeamLocation = teamLocationPersistence.saveTeamLocation(teamLocation);
        assertThat("TeamLocation was given an id", afterSaveTeamLocation.getId(), is(notNullValue()));

        teamLocationPersistence.deleteTeamLocation(teamLocation);
        teamLocation = teamLocationPersistence.reload(teamLocation);
        assertThat("TeamLocation was deleted", teamLocation, is(nullValue()));
    }

    private Team createTeam(Client client, int i) {
        Team team = new Team();
        team.setName("Test Team" + i);
        team.setDescription("Test Team description");
        team.setActive(i % 2 == 0);
        team.setClient(client);
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        team = teamPersistence.save(team);
        return team;
    }

    private Location createLocation(Client client, String uniqueId) {
        Location location = new Location();
        location.setName("Test Location " + uniqueId);
        location.setActive(true);
        location.setBelongsTo(client);
        location.setCity("CHICAGO");
        location.setPhone("630-222-8900");
        location.setState(State.IL);
        locationPersistence.save(location);
        return location;
    }

    private Client createClient(String uniqueId) {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("Test Client " + uniqueId);
        client.setType(clientType);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        clientPersistence.save(client);
        return client;
    }
    
    @Test
    public void saveTeamLocationWithOnlyTeamAndLocationId() {       
       Client client = createClient("1");
       Location location = createLocation(client, "1");
       Team team = createTeam(client, 1);
       
       TeamLocation teamLocation = new TeamLocation(new Location(location.getId()), new Team(team.getId()));
       TeamLocation afterSaveTeamLocation = teamLocationPersistence.saveTeamLocation(teamLocation);
       assertThat("TeamLocation was given an id", afterSaveTeamLocation.getId(), is(notNullValue()));
    }    
}
