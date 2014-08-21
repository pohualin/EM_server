package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.junit.Test;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Team Persistence Integration test 
 *
 */
public class TeamPersistenceIntegrationTest extends BaseIntegrationTest{
	
	 @Resource
	 ClientPersistence clientPersistence;
	 
	 @Resource
	 UserPersistence userPersistence;
	 
	 @Resource
	 TeamPersistence teamPersistence;
	 
	 /**
	  * Test saving a team under a client
	  */
	 @Test
	 public void testSaveTeam(){
		 Client client = makeClient();
		 clientPersistence.save(client);
		 
		 Team team = makeTeamForClient(client);
		 teamPersistence.save(team);
		 assertThat("Team was given an id", team.getId(), is(notNullValue()));
		 assertThat("client was given an id",client.getId(), is(notNullValue()));
	     assertThat("system is the created by", team.getCreatedBy(), is("system"));
	 }
	 
	 private Team makeTeamForClient(Client client){
		 Team team = new Team();
		 team.setName("Test Team");
		 team.setDescription("Test Team description");
		 team.setActive(false);
		 team.setPhone("1111111111");
		 team.setFax("2222222222");
		 team.setClient(client);
		 return team;
	 }
}
