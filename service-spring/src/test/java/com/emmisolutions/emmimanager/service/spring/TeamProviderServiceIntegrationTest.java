package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;
import com.emmisolutions.emmimanager.persistence.repo.TeamProviderRepository;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.UserService;

public class TeamProviderServiceIntegrationTest extends BaseIntegrationTest {

	@Resource
	ProviderService providerService;
	
	@Resource
	ClientService clientService;

	@Resource
	TeamService teamService;

	@Resource
	UserService userService;
	
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
	 */
	@Test
	public void testProviderSave() {

		Client client = makeClient("TeamProviFirst", "teamProUserTestOne");
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
		team.setSalesForceAccount(new TeamSalesForce("xxxWW"
				+ System.currentTimeMillis()));
        Team savedTeam = teamService.create(team);

        provider.setSpecialty(getSpecialty());
		provider = providerService.create(provider, savedTeam);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(),
				is("system"));
		
		Page<TeamProvider> providerPage = teamProviderService.findTeamProvidersByTeam(null, savedTeam);
		assertThat("TeamProvider was created", providerPage.getContent().iterator().next().getId(), is(notNullValue()));
	}

	protected Client makeClient(String clientName, String username) {
		Client client = new Client();
		client.setType(new ClientType(4l));
		client.setContractStart(LocalDate.now());
		client.setContractEnd(LocalDate.now().plusYears(1));
		client.setName(clientName);
		client.setContractOwner(userService.save(new User(username, "pw")));
		client.setSalesForceAccount(new SalesForce("xxxWW"
				+ System.currentTimeMillis()));
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
		Client client = makeClient("TeamProviTwo", "teamProUserTestTwo");
		clientService.create(client);

		Team team = new Team();
		team.setName("Addams Family Team");
		team.setDescription("Cute Show");
		team.setActive(false);
		team.setClient(client);
		team.setSalesForceAccount(new TeamSalesForce("xxxWW"
				+ System.currentTimeMillis()));
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

		TeamProvider providerToDelete = teamProviderPage.getContent().iterator().next();;
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
    @Test(expected = IllegalArgumentException.class)
    public void findByTeamBad(){
        teamProviderService.findTeamProvidersByTeam(null, null);
    }
    
    /**
     * Associate existing provider with a team
     */
    @Test
    public void testAssociateProvidersToTeam(){

    	//create a provider
    	Client client = makeClient("TeamTestProviderOne", "teamProUserTestOneTwenty");
		clientService.create(client);
		
    	Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);

		Team team = new Team();
		team.setName("Test Team Provider Gibber");
		team.setDescription("Test Team description");
		team.setActive(false);
		team.setClient(client);
		team.setSalesForceAccount(new TeamSalesForce("xxxWW"
				+ System.currentTimeMillis()));
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
		team2.setSalesForceAccount(new TeamSalesForce("xxxWW" + System.currentTimeMillis()));
        Team savedTeam2 = teamService.create(team2);
        List<Provider> providers = new ArrayList<Provider>();
        providers.add(provider);
        List<TeamProvider> teamProviders = teamProviderService.associateProvidersToTeam(providers, savedTeam2);
        assertThat("teamProvider was saved", teamProviders.iterator().next().getId(), is(notNullValue()));
    }
}
