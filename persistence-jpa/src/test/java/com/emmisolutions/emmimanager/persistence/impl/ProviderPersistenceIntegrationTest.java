package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProviderRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;

public class ProviderPersistenceIntegrationTest extends BaseIntegrationTest {

	@Resource
	ProviderPersistence providerPersistence;
	
	@Resource
	ProviderRepository providerRepository;
	
	@Resource
	ReferenceTagRepository referenceTagRepository;

	@Resource
	ReferenceGroupRepository referenceGroupRepository;
	
	@Resource
	ClientPersistence clientPersistence;
	
	@Resource
	UserPersistence userPersistence;
 
	@Resource
	TeamPersistence teamPersistence;

    @Resource
    ClientTypeRepository clientTypeRepository;
    
    @Resource
    ReferenceGroupTypeRepository referenceGroupTypeRepository;

	private User superAdmin;

	ClientType clientType;

 
	/**
	 * Before each test
	 */
	@Before
	public void init() {
		superAdmin = userPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
	}

	private Team makeNewTeam(int i){
		Client client = makeClient("Test Client" + i);
		clientPersistence.save(client);
		Team team = makeTeamForClient(client,i);
		teamPersistence.save(team);
		assertThat("Team was given an id", team.getId(), is(notNullValue()));
		assertThat("client was given an id",client.getId(), is(notNullValue()));
		return team;
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
	 * Testing a provider save
	 */
	@Test
	public void testProviderSave() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);
		provider.setSpecialty(getSpecialty());
		
		Team team = makeNewTeam(1);
		Set<Team> teams = new HashSet<Team>();
		teams.add(team);
		provider.setTeams(teams);
		provider = providerPersistence.save(provider);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(), is("system"));
		assertThat("Provider is part of the team:", provider.getTeams().iterator().next().getId(), is(notNullValue()));
	}

	/**
	 * Testing a provider save without the required fielf "last name"
	 */
	@Test(expected = ConstraintViolationException.class)
	public void testProviderSaveWithoutLastName() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(false);		
		provider.setSpecialty(getSpecialty());
		provider = providerPersistence.save(provider);
	}
	
	/**
	 * Testing a provider save with invalid character inside name fields, should fail
	 */
	@Test(expected = ConstraintViolationException.class)
	public void testProviderSaveWithInvalidCharactersForName() {
		Provider provider = new Provider();
		provider.setFirstName("Mary%");
		provider.setLastName("Poppins<3");
		provider.setMiddleName("middle name");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(false);
		provider.setSpecialty(getSpecialty());
		provider = providerPersistence.save(provider);
	}
	
	 private Client makeClient(String name) {
		 Client client = new Client();
	     client.setActive(false);
	     client.setName(name);
	     client.setType(clientType);
	     client.setRegion(new ClientRegion(1l));
	        client.setTier(new ClientTier(3l));
	     client.setContractOwner(superAdmin);
	     client.setContractStart(LocalDate.now());
	     client.setContractEnd(LocalDate.now().plusYears(2));
	     client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
	     return client;
	 }
	 
	 private Team makeTeamForClient(Client client, int i){
		 Team team = new Team();
		 team.setName("Test Team"+i);
		 team.setDescription("Test Team description");
		 team.setActive(i % 2 == 0);
		 team.setClient(client);
		 team.setSalesForceAccount(new TeamSalesForce("xxxWW" + System.currentTimeMillis()));
		 return team;
	 }
	 
	 /**
	 * Testing a provider save with team associated with it
	 */
 	@Test
	public void saveProviderWithTeam() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);
		provider.setSpecialty(getSpecialty());
		
		Team team1 = makeNewTeam(1);
		Set<Team> teams = new HashSet<Team>();
		teams.add(team1);
		provider.setTeams(teams);
		provider = providerPersistence.save(provider);

		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(), is("system"));
		assertThat("Provider belongs to team added", provider.getTeams().iterator().next().getId(), is(notNullValue()));
		
		Team team2 = makeNewTeam(2);
		provider.getTeams().add(team2);
		assertThat("Provider is a part of two teams", provider.getTeams().size(), is(2));
	}
 	
 	/**
	 * Testing a provider save without a team associated with it
	 */	 	
	@Test
	public void testProviderSaveWithoutATeam() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);
		provider.setSpecialty(getSpecialty());
		
		provider = providerPersistence.save(provider);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(),
				is("system"));
	}
 	
	/**
	 * Test find all providers for a given team
	 */
 	@Test
 	public void testGetProviderByTeam(){
 		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);
		provider.setSpecialty(getSpecialty());
		Team team1 = makeNewTeam(1);
		Set<Team> teams = new HashSet<Team>();
		teams.add(team1);
		provider.setTeams(teams);
		provider = providerPersistence.save(provider);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		Pageable page = new PageRequest(0, 50, Sort.Direction.ASC, "id");

		Page<Provider> providersForTeam = providerRepository.findByTeams(page, team1);
		
		assertThat("Provider was found for given team", providersForTeam.iterator().next().getId(), is(provider.getId()));
		assertThat("One provider was found for this team: ", providersForTeam.getContent().size(), is(1));
 	}
		
}
