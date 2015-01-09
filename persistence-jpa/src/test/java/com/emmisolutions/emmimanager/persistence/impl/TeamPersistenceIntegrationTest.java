package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.*;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.util.List;

import static com.emmisolutions.emmimanager.model.TeamSearchFilter.StatusFilter.ACTIVE_ONLY;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Team Persistence Integration test
 */
public class TeamPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    UserPersistence userPersistence;

    @Resource
    TeamPersistence teamPersistence;

    UserAdmin superAdmin;

    @Resource
    ClientTypeRepository clientTypeRepository;

    ClientType clientType;

    @Resource
    TeamTagPersistence teamTagPersistence;

    @Resource
    TagPersistence tagPersistence;

    @Resource
    GroupPersistence groupPersistence;

    /**
     * Initialization method
     */
    @Before
    public void init() {
        superAdmin = userPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
    }

    /**
     * Test saving a team under a client
     */
    @Test
    public void testSaveOneTeam() {
        Client client = makeClient();
        clientPersistence.save(client);

        Team team = makeTeamForClient(client, 1);
        teamPersistence.save(team);
        assertThat("Team was given an id", team.getId(), is(notNullValue()));
        assertThat("client was given an id", client.getId(), is(notNullValue()));
        assertThat("system is the created by", team.getCreatedBy(), is("system"));
        assertThat("Normalized Name", team.getNormalizedTeamName(), is("testteam1"));

        assertThat("can find by name and client id",
                teamPersistence.findByNormalizedTeamNameAndClientId("test Team 1", client.getId()),
                is(team));
        
    }

    /**
     * Test saving a list of teams under a client
     */
    @Test
    public void testListOfTeams() {
        Client client = makeClient();
        clientPersistence.save(client);

        for (int i = 0; i < 200; i++) {
            Team team = makeTeamForClient(client, i + 1);
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
        teamPage = teamPersistence.list(new PageRequest(10, 100), new TeamSearchFilter(client.getId(), ACTIVE_ONLY, "team5", "team9"));
        assertThat("only teams starting with 5 or 9 should come back", teamPage.getTotalElements(), is(10l));
        assertThat("there is 1 page", teamPage.getTotalPages(), is(1));
        assertThat("there is nothing on this page", teamPage.getNumberOfElements(), is(0));
        assertThat("there are 100 items in the page", teamPage.getSize(), is(100));
        assertThat("we are on page 10", teamPage.getNumber(), is(10));
        
        Team team = makeNewRandomTeam(client);
        Group group = makeNewRandomGroup(client);
        List<Tag> tags = makeNewRandomTags(group, 2);
        for (Tag tag : tags) {
            makeNewTeamTag(team, tag);
        }
        TeamSearchFilter filter = new TeamSearchFilter();
        filter.setTag(tags.get(0));
        Page<Team> teamPageA = teamPersistence.list(null, filter);
        assertThat("should return some teams", teamPageA.hasContent(), is(true));
    }

    /**
     * return teams with no teamtags
     */
    @Test
    public void getTeamsWithNoTeamTags() {
        TeamTag teamTag1 = new TeamTag();

        Client client1 = createClient("1");
        Group group = createGroup(client1, "ninjas");

        Tag tag1 = createTag(group);
        Team team1 = createTeam(client1, 1);
        Team team2 = createTeam(client1, 2);

        teamTag1.setTag(tag1);
        teamTag1.setTeam(team1);

        teamTagPersistence.saveTeamTag(teamTag1);

        TeamSearchFilter filter = new TeamSearchFilter(client1.getId(),TeamSearchFilter.StatusFilter.ACTIVE_ONLY,TeamSearchFilter.TeamTagType.UNTAGGED);

        Page<Team> returnedTeams = teamPersistence.list(null, filter);
        assertThat("team2 was returned", returnedTeams, hasItem(team2));
        assertThat("team1 was not returned", returnedTeams, not(hasItem(team1)));
    }

    /**
     * return inactive teams with no teamtags
     */
    @Test
    public void getInactiveTeamsWithNoTeamTags() {
        TeamTag teamTag1 = new TeamTag();

        Client client1 = createClient("1");
        Group group = createGroup(client1, "ninjas");

        Tag tag1 = createTag(group);
        Team team1 = createTeam(client1, 2);
        Team team2 = createTeam(client1, 1);

        teamTag1.setTag(tag1);
        teamTag1.setTeam(team1);

        teamTagPersistence.saveTeamTag(teamTag1);

        TeamSearchFilter filter = new TeamSearchFilter(client1.getId(),TeamSearchFilter.StatusFilter.INACTIVE_ONLY,TeamSearchFilter.TeamTagType.UNTAGGED);

        Page<Team> returnedTeams = teamPersistence.list(null, filter);
        assertThat("team2 was returned", returnedTeams, hasItem(team2));
        assertThat("team1 was not returned", returnedTeams, not(hasItem(team1)));
    }

    /**
     * return inactive teams with no teamtags
     */
    @Test
    public void getAllTeamsWithNoTeamTags() {
        TeamTag teamTag1 = new TeamTag();

        Client client1 = createClient("1");
        Group group = createGroup(client1, "ninjas");

        Tag tag1 = createTag(group);
        Team team1 = createTeam(client1, 2);
        Team team2 = createTeam(client1, 1);
        Team team3 = createTeam(client1, 2);

        teamTag1.setTag(tag1);
        teamTag1.setTeam(team1);

        teamTagPersistence.saveTeamTag(teamTag1);

        TeamSearchFilter filter = new TeamSearchFilter(client1.getId(),TeamSearchFilter.StatusFilter.ALL,TeamSearchFilter.TeamTagType.UNTAGGED);

        Page<Team> returnedTeams = teamPersistence.list(null, filter);
        assertThat("team2 was returned", returnedTeams, hasItem(team2));
        assertThat("team3 was returned", returnedTeams, hasItem(team3));
        assertThat("team1 was not returned", returnedTeams, not(hasItem(team1)));
    }

    /**
     * return teams with no teamtags and defined page
     */
    @Test
    public void getTeamsWithNoTeamTagsAndDefinedPage() {
        TeamTag teamTag1 = new TeamTag();

        Client client1 = createClient("1");
        Group group = createGroup(client1, "ninjas");

        Tag tag1 = createTag(group);
        Team team1 = createTeam(client1, 1);
        Team team2 = createTeam(client1, 2);

        teamTag1.setTag(tag1);
        teamTag1.setTeam(team1);

        teamTagPersistence.saveTeamTag(teamTag1);

        TeamSearchFilter filter = new TeamSearchFilter(client1.getId(),TeamSearchFilter.StatusFilter.ACTIVE_ONLY,TeamSearchFilter.TeamTagType.UNTAGGED);
        Page<Team> returnedTeams = teamPersistence.list(new PageRequest(0, 50, Sort.Direction.ASC, "id"), filter);
        assertThat("team2 was returned", returnedTeams, hasItem(team2));
        assertThat("team1 was not returned", returnedTeams, not(hasItem(team1)));
    }

    private Team createTeam(Client client, int i) {
        Team team = new Team();
        team.setName("Test Team" + i + RandomStringUtils.randomAlphanumeric(18));
        team.setDescription("Test Team description");
        team.setActive(i % 2 == 0);
        team.setClient(client);
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        team = teamPersistence.save(team);
        return team;
    }

    private Tag createTag(Group group) {
        Tag tag = new Tag();
        tag.setName("Test Tag " + RandomStringUtils.randomAlphanumeric(18));
        tag.setGroup(group);
        tag = tagPersistence.save(tag);
        return tag;
    }

    private Group createGroup(Client client, String name) {
        Group group = new Group();
        group.setName("Test Group" + name);
        group.setClient(client);
        group = groupPersistence.save(group);
        return group;
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

    private Team makeTeamForClient(Client client, int i) {
        Team team = new Team();
        team.setName("Test Team" + i);
        team.setDescription("Test Team description");
        team.setActive(i % 2 == 0);
        team.setClient(client);
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return team;
    }

    private Client makeClient() {
        Client client = new Client();
        client.setActive(false);
        client.setName("Test Client");
        client.setType(clientType);
        client.setRegion(new ClientRegion(1l));
        client.setTier(new ClientTier(3l));
        client.setContractOwner(superAdmin);
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(2));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return client;
    }
}
