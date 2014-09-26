package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.*;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

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

    private User superAdmin;


    @Before
    public void init() {
        superAdmin = userPersistence.reload("super_admin");
    }

    /**
     * Save success
     */
    @Test
    public void save() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient();
        Group group = createGroup(client);

        Tag tag = createTag(group,"");

        Team team = createTeam(client, 1);

        teamTag.setTag(tag);
        teamTag.setTeam(team);

        TeamTag afterSaveTeamTag = teamTagPersistence.saveTeamTag(teamTag);
        assertThat("TeamTag was given an id", afterSaveTeamTag.getId(), is(notNullValue()));
        assertThat("system is the created by", afterSaveTeamTag.getCreatedBy(), is("system"));
    }

    /*
     * try to save a null team tag
     */
    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void saveNull() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient();
        Group group = createGroup(client);

        Tag tag = createTag(group,"");

        Team team = createTeam(client, 1);

        teamTag.setTag(tag);
        teamTag.setTeam(team);

        TeamTag afterSaveTeamTag = teamTagPersistence.saveTeamTag(null);
    }


    /*
     * try to save a team tag with no team
     */
    @Test(expected=ConstraintViolationException.class)
    public void saveNullTeam() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient();
        Group group = createGroup(client);

        Tag tag = createTag(group,"");

        Team team = createTeam(client, 1);

        teamTag.setTag(tag);
        teamTag.setTeam(null);

        TeamTag afterSaveTeamTag = teamTagPersistence.saveTeamTag(teamTag);
    }

     /*
     * try to save a team tag with a team that doesnt exist
     */
    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void saveBadTeam() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient();
        Group group = createGroup(client);

        Tag tag = createTag(group,"");

        Team team = createTeam(client, 1);

        teamTag.setTag(tag);
        Team newTeam = new Team();
        newTeam.setName("Team Not in DB");
        teamTag.setTeam(newTeam);

        TeamTag afterSaveTeamTag = teamTagPersistence.saveTeamTag(teamTag);
    }

    /*
     * try to save a team tag with a tag that doesnt exist
     */
    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void saveBadTag() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient();
        Group group = createGroup(client);

        Tag tag = createTag(group,"");

        Team team = createTeam(client, 1);

        Tag newTag = new Tag();

        teamTag.setTag(newTag);
        teamTag.setTeam(team);

        TeamTag afterSaveTeamTag = teamTagPersistence.saveTeamTag(teamTag);
    }

    private Team createTeam(Client client, int i) {
        Team team = new Team();
        team.setName("Test Team"+i);
        team.setDescription("Test Team description");
        team.setActive(i % 2 == 0);
        team.setPhone("1111111111");
        team.setFax("2222222222");
        team.setClient(client);
        team = teamPersistence.save(team);
        return team;
    }

    private Tag createTag(Group group, String uniqueId ) {
        Tag tag = new Tag();
        tag.setName("Test Tag "+ uniqueId);
        tag.setGroup(group);
        tag = tagPersistence.save(tag);
        return tag;
    }

    private Group createGroup(Client client) {
        Group group = new Group();
        group.setName("Test Group");
        group.setClient(clientPersistence.reload(client.getId()));
        group = groupPersistence.save(group);
        return group;
    }

    private Client createClient() {
        Client client = new Client();
        client.setTier(ClientTier.THREE);
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(ClientRegion.NORTHEAST);
        client.setName("Test Client");
        client.setType(ClientType.PROVIDER);
        client.setActive(false);
        client.setContractOwner(superAdmin);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        clientPersistence.save(client);
        return client;
    }

    /**
     * Delete success
     */
    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void delete() {
        TeamTag teamTag = new TeamTag();

        Client client = createClient();
        Group group = createGroup(client);

        Tag tag = createTag(group,"");

        Team team = createTeam(client, 1);

        teamTag.setTag(tag);
        teamTag.setTeam(team);

        teamTag = teamTagPersistence.saveTeamTag(teamTag);
        assertThat("TeamTag was given an id", teamTag.getId(), is(notNullValue()));

        teamTagPersistence.deleteTeamTag(teamTag);
        teamTagPersistence.reload(teamTag);
    }

    /**
     * Test getting all the TeamTags for a given team
     */
    @Test
    public void getAllTeamTagsForTeam() {
        TeamTag teamTag1 = new TeamTag();
        TeamTag teamTag2 = new TeamTag();
        TeamTag teamTag3 = new TeamTag();

        Client client = createClient();
        Group group = createGroup(client);

        Tag tag1 = createTag(group,"1");
        Tag tag2 = createTag(group,"2");

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

        Page<TeamTag> associatedTeamTagsPage = teamTagPersistence.getAllTeamTagsForTeam(null,team1);
        assertThat("2 teams were correctly returned", associatedTeamTagsPage.getTotalElements(), is(2L));
    }

}
