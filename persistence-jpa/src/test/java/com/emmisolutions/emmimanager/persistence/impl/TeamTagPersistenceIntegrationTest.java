package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.*;
import com.emmisolutions.emmimanager.persistence.repo.ClientTypeRepository;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests TeamTagPersistence
 */
public class TeamTagPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    TeamTagPersistence teamTagPersistence;

    @Resource
    UserPersistence userPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    GroupPersistence groupPersistence;

    @Resource
    TagPersistence tagPersistence;

    @Resource
    TeamPersistence teamPersistence;

    User superAdmin;

    @Resource
    ClientTypeRepository clientTypeRepository;

    ClientType clientType;

    /**
     * this sets up the test
     */
    @Before
    public void init() {
        superAdmin = userPersistence.reload("super_admin");
        clientType = clientTypeRepository.getOne(1l);
    }

    /**
     * Save success
     */
    @Test
    public void save() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient("1");
        Group group = createGroup(client, "save");

        Tag tag = createTag(group, "");

        Team team = createTeam(client, 1);

        teamTag.setTag(tag);
        teamTag.setTeam(team);

        TeamTag afterSaveTeamTag = teamTagPersistence.saveTeamTag(teamTag);
        assertThat("TeamTag was given an id", afterSaveTeamTag.getId(), is(notNullValue()));
        assertThat("system is the created by", afterSaveTeamTag.getCreatedBy(), is("system"));
    }

    /**
     * try to save a null team tag
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveNullTeamTag() {
        teamTagPersistence.saveTeamTag(null);
    }

    /**
     * try to save a null team tag
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveNullTeamTagId() {
        teamTagPersistence.saveTeamTag(null);
    }

    /**
     * try to save a team tag with invalid team
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveNullTeam() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient("1");
        Group group = createGroup(client, "saveNullTeam");

        Tag tag = createTag(group, "");

        Team team = new Team();
        team.setName("Test Team");
        team.setDescription("Test Team description");
        team.setActive(true);
        team.setClient(client);

        teamTag.setTag(tag);
        teamTag.setTeam(team);
        TeamTag afterSaveTeamTag = teamTagPersistence.saveTeamTag(teamTag);
        assertThat("afterSaveTeamTag was null", afterSaveTeamTag, is(nullValue()));
    }

    /**
     * try to save a team tag with invalid team
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveNullTag() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient("1");
        Group group = createGroup(client, "saveNullTag");

        Tag tag = new Tag();
        tag.setName("Test Tag");
        tag.setGroup(group);

        Team team = createTeam(client, 1);

        teamTag.setTag(tag);
        teamTag.setTeam(team);
        TeamTag afterSaveTeamTag = teamTagPersistence.saveTeamTag(teamTag);
        assertThat("afterSaveTeamTag was null", afterSaveTeamTag, is(nullValue()));
    }

    /**
     * try to save a team tag with a team that has a different client than the tag
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveDifferentClientTeam() {
        TeamTag teamTag = new TeamTag();

        Client client1 = createClient("1");
        Client client2 = createClient("2");
        Group group = createGroup(client1, "saveDifferentClientTeam");

        Tag tag = createTag(group, "");

        Team team = createTeam(client2, 1);

        teamTag.setTag(tag);
        teamTag.setTeam(team);

        teamTagPersistence.saveTeamTag(teamTag);
    }

    /**
     * try to save a team tag with a tag that has a different client than the team
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void saveDifferentClientTag() {
        TeamTag teamTag = new TeamTag();

        Client client1 = createClient("1");
        Client client2 = createClient("2");
        Group group = createGroup(client2, "saveDifferentClientTag");

        Tag tag = createTag(group, "");

        Team team = createTeam(client1, 1);

        teamTag.setTag(tag);
        teamTag.setTeam(team);

        teamTagPersistence.saveTeamTag(teamTag);
    }


    /**
     * Delete success
     */
    @Test
    public void delete() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient("1");
        Group group = createGroup(client, "delete");

        Tag tag = createTag(group, "");

        Team team = createTeam(client, 1);

        teamTag.setTag(tag);
        teamTag.setTeam(team);

        teamTag = teamTagPersistence.saveTeamTag(teamTag);
        assertThat("TeamTag was given an id", teamTag.getId(), is(notNullValue()));

        teamTagPersistence.deleteTeamTag(teamTag);
        teamTag = teamTagPersistence.reload(teamTag);
        assertThat("TeamTag was deleted", teamTag, is(nullValue()));
    }

    /**
     * Test getting all the TeamTags for a given team
     */
    @Test
    public void getAllTeamTagsForTeam() {
        TeamTag teamTag1 = new TeamTag();
        TeamTag teamTag2 = new TeamTag();
        TeamTag teamTag3 = new TeamTag();

        Client client = createClient("1");
        Group group = createGroup(client, "getAllTeamTagsForTeam");

        Tag tag1 = createTag(group, "1");
        Tag tag2 = createTag(group, "2");

        Team team1 = createTeam(client, 1);
        Team team3 = createTeam(client, 3);

        teamTag1.setTag(tag1);
        teamTag2.setTag(tag2);
        teamTag3.setTag(tag2);
        teamTag1.setTeam(team1);
        teamTag2.setTeam(team1);
        teamTag3.setTeam(team3);

        teamTagPersistence.saveTeamTag(teamTag1);
        teamTagPersistence.saveTeamTag(teamTag2);
        teamTagPersistence.saveTeamTag(teamTag3);
        assertThat("TeamTag was given an id", teamTag1.getId(), is(notNullValue()));
        assertThat("TeamTag was given an id", teamTag2.getId(), is(notNullValue()));
        assertThat("TeamTag was given an id", teamTag3.getId(), is(notNullValue()));

        Page<TeamTag> associatedTeamTagsPage = teamTagPersistence.getAllTeamTagsForTeam(null, team1);
        assertThat("2 teams were correctly returned", associatedTeamTagsPage.getTotalElements(), is(2L));
    }

    /**
     * delete all teamTags with a given team
     */
    @Test
    public void deleteTeamTagsWithTeam() {
        TeamTag teamTag1 = new TeamTag();
        TeamTag teamTag2 = new TeamTag();
        TeamTag teamTag3 = new TeamTag();

        Client client1 = createClient("1");
        Group group = createGroup(client1, "deleteTeamTagsWithTeam");

        Tag tag1 = createTag(group, "1");
        Tag tag2 = createTag(group, "2");
        Team team1 = createTeam(client1, 1);
        Team team2 = createTeam(client1, 2);

        teamTag1.setTag(tag1);
        teamTag1.setTeam(team1);

        teamTag2.setTag(tag1);
        teamTag2.setTeam(team2);

        teamTag3.setTag(tag2);
        teamTag3.setTeam(team1);

        teamTagPersistence.saveTeamTag(teamTag1);
        teamTagPersistence.saveTeamTag(teamTag2);
        teamTagPersistence.saveTeamTag(teamTag3);

        teamTagPersistence.deleteTeamTagsWithTeam(team1);
        assertThat("teamtag1 was deleted", teamTagPersistence.reload(teamTag1), is(nullValue()));
        assertThat("teamtag3 was deleted", teamTagPersistence.reload(teamTag3), is(nullValue()));
        assertThat("teamtag2 was not deleted", teamTagPersistence.reload(teamTag2), is(notNullValue()));
    }

    /**
     * try to save a team tag with a tag that has a different client than the team
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void getAllTeamsForTagNull() {
        teamTagPersistence.deleteTeamTagsWithTeam(null);
    }

    /**
     * Creates a Client, Team, a couple of Groups with Tags. Testing that the remove method
     * works for tags on one of the groups but not the other.
     */
    @Test
    public void removeTeamTags() {
        Client client = createClient("team tag remove case");
        Team team = createTeam(client, 4);

        final Group groupToBeSaved = createGroup(client, "removeTeamTags1");
        Tag savedTag = createTag(groupToBeSaved, "shouldStillBeThere");
        TeamTag shouldBeSaved = teamTagPersistence.saveTeamTag(new TeamTag(team, savedTag));

        Group groupToBeDeleted = createGroup(client, "removeTeamTags2");
        Tag shouldBeRemovedTag = createTag(groupToBeDeleted, "toBeDeleted");
        teamTagPersistence.saveTeamTag(new TeamTag(team, shouldBeRemovedTag));

        assertThat("there should be two TeamTags for the team",
                teamTagPersistence.getAllTeamTagsForTeam(null, team).getTotalElements(), is(2l));

        teamTagPersistence.removeTeamTagsThatAreNotAssociatedWith(client.getId(), new HashSet<Long>() {{
            add(groupToBeSaved.getId());
        }});

        Page<TeamTag> teamTagPage = teamTagPersistence.getAllTeamTagsForTeam(null, team);
        assertThat("there should be one TeamTag for the team", teamTagPage.getTotalElements(), is(1l));
        assertThat("shouldBeSaved TeamTag should be present", teamTagPage, hasItem(shouldBeSaved));

        teamTagPersistence.removeTeamTagsThatAreNotAssociatedWith(client.getId(), new HashSet<Long>());
        assertThat("there should be no remaining team tags", teamTagPage.getTotalElements(), is(0l));

    }

    /**
     * Remove of nothing should yield zero as a result
     */
    @Test
    public void removeNothing() {
        assertThat("nothing gets removed", teamTagPersistence.removeTeamTagsThatAreNotAssociatedWith(null, null), is(0l));
    }

    private Team createTeam(Client client, int i) {
        Team team = new Team();
        team.setName("Test Team" + i);
        team.setDescription("Test Team description");
        team.setActive(i % 2 == 0);
        team.setClient(client);
        team.setSalesForceAccount(new TeamSalesForce("xxxWW" + System.currentTimeMillis()));
        team = teamPersistence.save(team);
        return team;
    }

    private Tag createTag(Group group, String uniqueId) {
        Tag tag = new Tag();
        tag.setName("Test Tag " + uniqueId);
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
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        clientPersistence.save(client);
        return client;
    }

}
