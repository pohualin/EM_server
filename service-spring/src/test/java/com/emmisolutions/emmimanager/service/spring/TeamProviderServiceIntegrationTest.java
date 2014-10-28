package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;
import com.emmisolutions.emmimanager.persistence.repo.TeamProviderRepository;
import com.emmisolutions.emmimanager.service.*;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;

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
}
