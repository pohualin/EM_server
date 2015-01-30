package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter.StatusFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;
import com.emmisolutions.emmimanager.persistence.repo.TeamProviderRepository;
import com.emmisolutions.emmimanager.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TeamProviderServiceIntegrationTest extends BaseIntegrationTest {

	@Resource
	ProviderService providerService;

	@Resource
	ClientService clientService;

	@Resource
	TeamService teamService;

	@Resource
    UserAdminService userAdminService;

	@Resource
	ReferenceTagRepository referenceTagRepository;

	@Resource
	ReferenceGroupRepository referenceGroupRepository;

	@Resource
    ReferenceGroupTypeRepository referenceGroupTypeRepository;

	@Resource
	TeamProviderRepository teamProviderRepository;

	@Resource
	TeamProviderService teamProviderService;

	@Resource
    LocationService locationService;

	@Resource
	TeamLocationService teamLocationService;

	@Resource
	ClientProviderService clientProviderService;

	@Resource
	TeamProviderTeamLocationService teamProviderTeamLocationService;
	/**
	 * Testing a provider save without a persistent team.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createProviderWithUnsavedTeam() {
		Provider provider = new Provider();
		Team team = new Team();
		providerService.create(provider, team);
	}

	/**
	 * Testing a provider save with team, verify that teamProvider is created
     * by two different search patterns
	 */
	@Test
	public void testProviderSave() {
        logout(); // making default of 'system' the user

		Client client = makeClient("TeamProviFirst");
		clientService.create(client);

		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);

		Team team = new Team();
		team.setName("Test Team Provider");
		team.setDescription("Test Team description");
		team.setActive(false);
		team.setClient(client);
		team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        Team savedTeam = teamService.create(team);

        provider.setSpecialty(getSpecialty());
		provider = providerService.create(provider, savedTeam);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(),
				is("system"));

		Page<TeamProvider> providerPage = teamProviderService.findTeamProvidersByTeam(null, savedTeam);
		assertThat("TeamProvider was created", providerPage.getContent().iterator().next().getId(), is(notNullValue()));

        Page<Team> foundByDifferentFinder = teamProviderService.findTeamsBy(client, provider,  null);

        assertThat("teams should be equal", foundByDifferentFinder.iterator().next(), is(providerPage.iterator().next().getTeam()));

	}

	protected Client makeClient(String clientName) {
		Client client = new Client();
		client.setType(new ClientType(4l));
		client.setContractStart(LocalDate.now());
		client.setContractEnd(LocalDate.now().plusYears(1));
		client.setName(clientName);
		client.setContractOwner(new UserAdmin(1l, 0));
		client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
		return client;
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

	/**
	 * Test deletion of TeamProvider, verify Provider still exists
	 */
	@Test
	public void createProviderForTeamThenDeleteAssociation(){
		Client client = makeClient("TeamProviTwo");
		clientService.create(client);

		Team team = new Team();
		team.setName("Addams Family Team");
		team.setDescription("Cute Show");
		team.setActive(false);
		team.setClient(client);
		team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        Team savedTeam = teamService.create(team);

		Provider provider = new Provider();
		provider.setFirstName("Morticia");
		provider.setLastName("Addams");
		provider.setEmail("morticiaaddams@fourtysecondstreet.com");
		provider.setActive(true);
        provider.setSpecialty(getSpecialty());
		provider = providerService.create(provider, savedTeam);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));

		//verify that TeamProvider was created
		Pageable page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
		Page<TeamProvider> teamProviderPage = teamProviderService.findTeamProvidersByTeam(page, savedTeam);
		assertThat("teamProviders were found", teamProviderPage.getContent().size(), is(notNullValue()));

		TeamProvider providerToDelete = teamProviderPage.getContent().iterator().next();
		teamProviderService.delete(providerToDelete);
		Page<TeamProvider> teamProviderPageNew = teamProviderService.findTeamProvidersByTeam(page, savedTeam);

		assertThat("Provider still exists", provider.getId(), is(notNullValue()));
		assertThat("teamProvider was deleted", teamProviderPageNew.getContent().size(), is(0));
	}

    /**
     * Reload of null is null (not an exception)
     */
    @Test
    public void reloadNull(){
        assertThat("Reload of null returns null", teamProviderService.reload(null), is(nullValue()));
    }

    /**
     * Shouldn't be able to find providers for a team that doesn't exist
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void findByTeamBad(){
        teamProviderService.findTeamProvidersByTeam(null, null);
    }

    /**
     * Associate existing provider with a team, then remove it by Client and Provider
     */
    @Test
    public void testAssociateProvidersToTeam(){

    	//create a provider
    	Client client = makeClient("TeamTestProviderOne");
		clientService.create(client);

    	Provider provider = new Provider();
		provider.setFirstName(RandomStringUtils.randomAlphabetic(255));
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);

		Team team = new Team();
		team.setName("Test Team Provider Gibber");
		team.setDescription("Test Team description");
		team.setActive(false);
		team.setClient(client);
		team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        Team savedTeam = teamService.create(team);
        provider.setSpecialty(getSpecialty());
		provider = providerService.create(provider, savedTeam);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));

	    //new team to associate to the existing provider
		Team team2 = new Team();
		team2.setName("Test Team Provider");
		team2.setDescription("Test Team description");
		team2.setActive(false);
		team2.setClient(client);
		team2.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        Team savedTeam2 = teamService.create(team2);
        Set<Provider> providers = new HashSet<>();
        providers.add(provider);


        final TeamProviderTeamLocationSaveRequest request = new TeamProviderTeamLocationSaveRequest();
        request.setProvider(provider);
        Set<TeamProvider> teamProviders = teamProviderService.associateProvidersToTeam(new ArrayList<TeamProviderTeamLocationSaveRequest>() {{
    		add(request);
    	}}, savedTeam2);
        assertThat("teamProvider was saved", teamProviders.iterator().next().getId(), is(notNullValue()));
        assertThat("two team providers were removed", teamProviderService.delete(client, provider), is(2l));
        assertThat("teamProviders are deleted", teamProviderService.findTeamProvidersByTeam(null, savedTeam).getTotalElements(), is(0l));
        
        ProviderSearchFilter providerSearchFilter = new ProviderSearchFilter(StatusFilter.ACTIVE_ONLY, provider.getFirstName());
        Page<TeamProvider> tproviders = teamProviderService.findPossibleProvidersToAdd(savedTeam2, providerSearchFilter, null);
        assertThat("teamProviders are found", tproviders.getTotalElements(), is(1l));
        assertThat("teamProviders are found", tproviders.iterator().next().getProvider().getId(), is(notNullValue()));

    }

    /**
     * Associate existing provider with an invalid team
     */
    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void testFindPossibleProvidersToAddWithNullTeam(){
        ProviderSearchFilter providerSearchFilter = new ProviderSearchFilter(StatusFilter.ACTIVE_ONLY, "mary");
        teamProviderService.findPossibleProvidersToAdd(null, providerSearchFilter, null);
    }
    /**
     * Associate existing provider with an invalid team
     */
    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void testAssociateProvidersToAnInvalidTeam(){

    	//create a provider
    	Client client = makeClient("TeamTestProviderOneOne");
		clientService.create(client);

    	Provider provider = new Provider();
		provider.setFirstName("Officer");
		provider.setMiddleName("Broadway");
		provider.setLastName("Krupke");
		provider.setEmail("officerKrupke@fourtysecondstreet.com");
		provider.setActive(true);

		Team team = new Team();
		team.setName("Provider Gibber ish");
		team.setDescription("Test Team description");
		team.setActive(false);
		team.setClient(client);
		team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        Team savedTeam = teamService.create(team);
        provider.setSpecialty(getSpecialty());
		provider = providerService.create(provider, savedTeam);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));

	    //null team to associate to the existing provider
		Team team2 = new Team();
        Set<Provider> providers = new HashSet<>();
        providers.add(provider);

        final TeamProviderTeamLocationSaveRequest request = new TeamProviderTeamLocationSaveRequest();
        request.setProvider(provider);

        Set<TeamProvider> teamProviders = teamProviderService.associateProvidersToTeam(new ArrayList<TeamProviderTeamLocationSaveRequest>() {{
    		add(request);
    	}}, team2);
        assertThat("teamProvider was saved", teamProviders.iterator().next().getId(), is(notNullValue()));

    }

    /**
     * Associate existing provider with a team and a bunch of team locations
     */
    @Test
    public void testAssociateProvidersToTeamWithTeamLocations(){

    	//create a provider
    	Client client = makeClient("Mama Morton");
		clientService.create(client);

    	Provider provider = new Provider();
		provider.setFirstName("Amos");
		provider.setMiddleName("Invisible");
		provider.setLastName("Hart");
		provider.setEmail("amosHart@fourtysecondstreet.com");
		provider.setActive(true);

		Team team = new Team();
		team.setName("The musical team");
		team.setDescription("We sing");
		team.setActive(false);
		team.setClient(client);
		team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        Team savedTeam = teamService.create(team);
        provider.setSpecialty(getSpecialty());
		provider = providerService.create(provider, savedTeam);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));

	    //new team to associate to the existing provider
		Team team2 = new Team();
		team2.setName("Chicago");
		team2.setDescription("In the city");
		team2.setActive(false);
		team2.setClient(client);
		team2.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        Team savedTeam2 = teamService.create(team2);
        List<Provider> providers = new ArrayList<>();
        providers.add(provider);


        Location locationOne = locationService.create( makeLocation("Location One", "1") );
        TeamLocation teamLocationOne = new TeamLocation();
        teamLocationOne.setLocation(locationOne);
        teamLocationOne.setTeam(team2);

        Location locationTwo = locationService.create( makeLocation("Location Two", "2") );
        TeamLocation teamLocationTwo = new TeamLocation();
        teamLocationTwo.setLocation(locationTwo);
        teamLocationTwo.setTeam(team2);

        Set<TeamLocationTeamProviderSaveRequest> reqs = new HashSet<>();
        TeamLocationTeamProviderSaveRequest req = new TeamLocationTeamProviderSaveRequest();
        req.setLocation(locationOne);
        reqs.add(req);

        req = new TeamLocationTeamProviderSaveRequest();
        req.setLocation(locationTwo);
        reqs.add(req);

        teamLocationService.save(team2, reqs);

        Page<TeamLocation> teamLocationPage = teamLocationService.findAllTeamLocationsWithTeam(null,team2);

        final TeamProviderTeamLocationSaveRequest request = new TeamProviderTeamLocationSaveRequest();
        request.setProvider(provider);
        request.setTeamLocations(new HashSet<>(teamLocationPage.getContent()));

        Set<TeamProvider> teamProviders = teamProviderService.associateProvidersToTeam(new ArrayList<TeamProviderTeamLocationSaveRequest>() {{
    		add(request);
    	}}, savedTeam2);

        Page<ClientProvider> clientProviders = clientProviderService.findByClient(client, null);

        assertThat("One Client Provider was saved", clientProviders.getContent().size(), is(1));
        assertThat("teamProvider was saved", teamProviders.iterator().next().getId(), is(notNullValue()));

        Set<TeamLocation> tls = new HashSet<>();
        provider.setFirstName("New First Name");
        request.setTeamLocations(tls);
        teamProviderService.updateTeamProvider(request);

        Page<TeamProvider> foundTeamProvider = teamProviderService.findTeamProvidersByTeam(null, team2);
        assertThat("TeamProvider found", foundTeamProvider.hasContent(), is(true));
        assertThat("Updated provider first name", foundTeamProvider.getContent().get(0).getProvider().getFirstName(), is("New First Name"));
        
        assertThat("tptl's found:", teamProviderService.findTeamLocationsByTeamProvider(foundTeamProvider.getContent().get(0), null), is(notNullValue()));
        
    }

    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void invalidFindByClientAndProvider(){
        teamProviderService.findTeamsBy(null, null, null);
    }

    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void invalidFindByTeam(){
        teamProviderService.findTeamProvidersByTeam(null, new Team());
    }

    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void invalidDeleteByClientAndProvider(){
        teamProviderService.delete(null, null);
    }

    @Test
    public void testTeamProviderTeamLocationRelationship(){

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
