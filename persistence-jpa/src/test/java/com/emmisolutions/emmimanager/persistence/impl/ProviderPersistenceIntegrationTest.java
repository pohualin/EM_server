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

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Gender;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;

public class ProviderPersistenceIntegrationTest extends BaseIntegrationTest {

	@Resource
	ProviderPersistence providerPersistence;
	
	@Resource
	ClientPersistence clientPersistence;
	
	@Resource
	UserPersistence userPersistence;
 
	@Resource
	TeamPersistence teamPersistence;
 
	private User superAdmin;
 
	/**
	 * Before each test
	 */
	@Before
	public void init() {
		superAdmin = userPersistence.reload("super_admin");
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
	 
	@Test
	public void testProviderSave() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setGender(Gender.FEMALE);
		provider.setActive(true);
		Team team = makeNewTeam(1);
		Set<Team> teams = new HashSet<Team>();
		teams.add(team);
		provider.setTeams(teams);
		provider = providerPersistence.save(provider);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(), is("system"));
		assertThat("Provider is part of the team:", provider.getTeams().iterator().next().getId(), is(notNullValue()));
	}

	@Test(expected = ConstraintViolationException.class)
	public void testProviderSaveWithoutMiddleName() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setGender(Gender.FEMALE);
		provider.setActive(false);
		provider = providerPersistence.save(provider);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testProviderSaveWithInvalidCharactersForName() {
		Provider provider = new Provider();
		provider.setFirstName("Mary%");
		provider.setLastName("Poppins<3");
		provider.setMiddleName("middle name");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setGender(Gender.FEMALE);
		provider.setActive(false);
		provider = providerPersistence.save(provider);
	}
	
	 private Client makeClient(String name) {
		 Client client = new Client();
	     client.setActive(false);
	     client.setName(name);
	     client.setType(ClientType.PROVIDER);
	     client.setRegion(ClientRegion.NORTHEAST);
	     client.setTier(ClientTier.THREE);
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
	 
	 //there should be a addTeam() on ProviderService
 	@Test
	public void testProviderSaveNewTeamForProvider() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setGender(Gender.FEMALE);
		provider.setActive(true);

		Team team1 = makeNewTeam(1);
		Set<Team> teams = new HashSet<Team>();
		teams.add(team1);
		provider.setTeams(teams);
		provider = providerPersistence.save(provider);

		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(), is("system"));
		assertThat("Provider belongs to team added", provider.getTeams().iterator().next().getId(), is(notNullValue()));
		
		//add second team to provider
		Team team2 = makeNewTeam(2);
		provider.getTeams().add(team2);
		assertThat("Provider is a part of two teams", provider.getTeams().size(), is(2));
	}
	 	
	@Test
	public void testProviderSaveWithoutATeam() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setGender(Gender.FEMALE);
		provider.setActive(true);
		provider = providerPersistence.save(provider);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(),
				is("system"));
	}

		//there should be a removeTeam() on ProviderService

		//remove team from provider
	
	
 	@Test
	public void testRemoveTeamForProvider() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setGender(Gender.FEMALE);
		provider.setActive(true);

		Team team1 = makeNewTeam(1);
		Set<Team> teams = new HashSet<Team>();
		teams.add(team1);
		provider.setTeams(teams);
		provider = providerPersistence.save(provider);

		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(), is("system"));
		assertThat("Provider belongs to team added", provider.getTeams().iterator().next().getId(), is(notNullValue()));
		
		//add second team to provider
		Team team2 = makeNewTeam(2);
		provider.getTeams().add(team2);
		assertThat("Provider is a part of two teams", provider.getTeams().size(), is(2));
		
		//removeTeamFromProvider
		//incomplete
	}
		
}
