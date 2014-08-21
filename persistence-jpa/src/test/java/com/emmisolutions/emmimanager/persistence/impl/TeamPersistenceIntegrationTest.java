package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;

import static com.emmisolutions.emmimanager.model.TeamSearchFilter.StatusFilter.ACTIVE_ONLY;
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
	 public void testSaveOneTeam(){
		 Client client = makeClient();
		 clientPersistence.save(client);
		 
		 Team team = makeTeamForClient(client,1);
		 teamPersistence.save(team);
		 assertThat("Team was given an id", team.getId(), is(notNullValue()));
		 assertThat("client was given an id",client.getId(), is(notNullValue()));
	     assertThat("system is the created by", team.getCreatedBy(), is("system"));
	 }
	 
	 /**
	  * Test saving a list of teams under a client
	  */
	 @Test
	 public void testListOfTeams(){
		 Client client = makeClient();
		 clientPersistence.save(client);
		 
		 for(int i=0;i<200;i++){
			 Team team = makeTeamForClient(client,i+1);
			 teamPersistence.save(team);
		 }
		 		 
		 Page<Team> teamPage = teamPersistence.list(null, null);
	     assertThat("found all of the clients", teamPage.getTotalElements(), is(200l));
	     assertThat("there were 4 pages", teamPage.getTotalPages(), is(4));
	     assertThat("there are 50 items in the page", teamPage.getSize(), is(50));
	     assertThat("we are on page 0", teamPage.getNumber(), is(0));
	     
	     // test list with filters
	     String[] nameFilters = new String[]{"Test"};
	     teamPage = teamPersistence.list(new PageRequest(2, 10), new TeamSearchFilter(nameFilters));
	     assertThat("found all of the teams", teamPage.getTotalElements(), is(200l));
	     assertThat("there were 20 pages", teamPage.getTotalPages(), is(20));
	     assertThat("there are 10 items in the page", teamPage.getSize(), is(10));
	     assertThat("we are on page 2", teamPage.getNumber(), is(2));
	     
	     // search for active, page size 100, multiple names
	     teamPage = teamPersistence.list(new PageRequest(0, 100), new TeamSearchFilter(ACTIVE_ONLY, "team5", "team9"));
	     assertThat("only teams starting with 5 or 9 should come back", teamPage.getTotalElements(), is(10l));
	     assertThat("there is 1 page", teamPage.getTotalPages(), is(1));
	     assertThat("there are 10 elements on this page", teamPage.getNumberOfElements(), is(10));
	     assertThat("there are 100 items in the page", teamPage.getSize(), is(100));
	     assertThat("we are on page 0", teamPage.getNumber(), is(0));

	     // request a page out of bounds
	     teamPage = teamPersistence.list(new PageRequest(10, 100), new TeamSearchFilter(ACTIVE_ONLY, "team5", "team9"));
	     assertThat("only teams starting with 5 or 9 should come back", teamPage.getTotalElements(), is(10l));
	     assertThat("there is 1 page", teamPage.getTotalPages(), is(1));
	     assertThat("there is nothing on this page", teamPage.getNumberOfElements(), is(0));
	     assertThat("there are 100 items in the page", teamPage.getSize(), is(100));
	     assertThat("we are on page 10", teamPage.getNumber(), is(10));
	 }
	 
	 private Team makeTeamForClient(Client client, int i){
		 Team team = new Team();
		 team.setName("Test Team"+i);
		 team.setDescription("Test Team description");
		 team.setActive(i % 2 == 0);
		 team.setPhone("1111111111");
		 team.setFax("2222222222");
		 team.setClient(client);
		 return team;
	 }
}
