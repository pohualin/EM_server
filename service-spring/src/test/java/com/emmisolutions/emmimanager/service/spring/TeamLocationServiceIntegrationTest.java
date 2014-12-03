package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.State;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamLocationTeamProviderSaveRequest;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientLocationService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.LocationService;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamLocationService;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.UserService;

/**
 * Team Location Service Integration test
 */
public class TeamLocationServiceIntegrationTest extends BaseIntegrationTest {

	@Resource
	ProviderService providerService;
	
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

	@Resource
	ReferenceTagRepository referenceTagRepository;
	
	@Resource
    ReferenceGroupTypeRepository referenceGroupTypeRepository;
	
	@Resource
	ReferenceGroupRepository referenceGroupRepository;
	
	@Resource
	TeamProviderService teamProviderService;
	
	@Resource
	TeamProviderTeamLocationService teamProviderTeamLocationService;
	
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

    	Provider provider = new Provider();
		provider.setFirstName("Amos");
		provider.setMiddleName("Invisible");
		provider.setLastName("Hart");
		provider.setEmail("amosHart@fourtysecondstreet.com");
		provider.setActive(true);
        provider.setSpecialty(getSpecialty());
		provider = providerService.create(provider, savedTeam);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));        
        
		Page<TeamProvider> providerPage = teamProviderService.findTeamProvidersByTeam(null, savedTeam);
		TeamProvider teamProvider = providerPage.getContent().iterator().next();
		assertThat("TeamProvider was created", teamProvider.getId(), is(notNullValue()));
		
        Location location = locationService.create( makeLocation("Location ", "1") );
        assertThat("location was created successfully", location.getId(), is(notNullValue()));

        Set<TeamLocationTeamProviderSaveRequest> reqs = new HashSet<TeamLocationTeamProviderSaveRequest>();
        TeamLocationTeamProviderSaveRequest req = new TeamLocationTeamProviderSaveRequest();
        Set<TeamProvider> tps = new HashSet<TeamProvider>();
        
        tps.add(teamProvider);
        req.setProviders(tps);
        req.setLocation(location);
        reqs.add(req);

        assertThat("there is no client location yet", clientLocationPersistence.reload(location.getId(), client.getId()), is(nullValue()));
      
        // create the team location
        teamLocationService.save(savedTeam, reqs);

        Page<TeamLocation> locationPage = teamLocationService.findAllTeamLocationsWithTeam(null, savedTeam);
        TeamLocation teamLocation = locationPage.getContent().iterator().next();
		assertThat("TeamLocation was created", teamLocation.getId(), is(notNullValue()));

		Page<TeamProviderTeamLocation> tptlPage = teamProviderTeamLocationService.findByTeamLocation(teamLocation, null);
		TeamProviderTeamLocation tptl = tptlPage.getContent().iterator().next();
	    assertThat("TeamProvider was associated to the Team succesfully", tptl.getTeamProvider(), is(teamProvider));
						
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
    
	private ReferenceTag getSpecialty(){
		ReferenceTag specialty = new ReferenceTag();
		ReferenceGroup group = new ReferenceGroup();
		ReferenceGroupType type = new ReferenceGroupType();
		type.setName("refGroupType");
		type= referenceGroupTypeRepository.save(type);
		group.setName("ref group");
		group.setType(type);
		group = referenceGroupRepository.save(group);
		specialty.setName("ENT");
		specialty.setGroup(group);
		specialty = referenceTagRepository.save(specialty);
		return specialty;
	}
}
