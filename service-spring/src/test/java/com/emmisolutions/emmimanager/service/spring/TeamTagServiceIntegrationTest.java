package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TeamTagServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    TeamTagService teamTagService;

    @Resource
    ClientService clientService;

    @Resource
    GroupService groupService;

    @Resource
    TagService tagService;

    @Resource
    TeamService teamService;

    @Resource
    UserService userService;


    private Client createClient() {
        UserAdmin user = new UserAdmin(1l, 0);

        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName("Test Client " + RandomStringUtils.randomAlphanumeric(18));
        client.setType(new ClientType(1l));
        client.setContractOwner(user);
        client.setActive(false);
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        clientService.create(client);
        return client;
    }

    private Group createGroup(Client client) {
        Group group = new Group();
        group.setName("Test Group" + RandomStringUtils.randomAlphanumeric(9));
        group.setClient(clientService.reload(client));
        groupService.save(group);
        return group;
    }

    private Tag createTag(Group group) {
        Tag tag = new Tag();
        tag.setName("Test Tag " + RandomStringUtils.randomAlphanumeric(9));
        tag.setGroup(group);
        return tag;
    }

    private Team createTeam(Client client) {
        Team team = new Team();
        team.setName("Test Team" + RandomStringUtils.randomAlphanumeric(18));
        team.setDescription("Test Team description");
        team.setActive(true);
        team.setClient(client);
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        teamService.create(team);
        return team;
    }

    /**
     * Test List tags by group id
     */
    @Test
    public void testSaveAndFind() {
        Client client = createClient();
        Group group = createGroup(client);

        List<Tag> tagList = createTagList(group, 2);

        Team team1 = createTeam(client);

        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tagList.get(0));
        tagSet.add(tagList.get(1));

        teamTagService.save(team1, tagSet);

        Page<TeamTag> teamTagPage = teamTagService.findAllTeamTagsWithTeam(null, team1);
        assertThat("teamTagPage has two teams", teamTagPage.getTotalElements(), is(2L));
    }

    private List<Tag> createTagList(Group group, int numberOfTags) {
        List<Tag> tagList = new ArrayList<>();
        for (int i = 0; i < numberOfTags; i++) {
            Tag tag = createTag(group);
            tagList.add(tag);
        }

        tagList = tagService.saveAllTagsForGroup(tagList, group);
        return tagList;
    }

    /**
     * Test the save method deletes items in between saves
     */
    @Test
    public void testSaveDeletesItems() {
        Client client = createClient();
        Group group = createGroup(client);

        // create 4 tags in a group
        List<Tag> tagListComplete = createTagList(group, 4);

        // split into two groups
        List<Tag> tagListFirstHalf = tagListComplete.subList(0, 2);
        List<Tag> tagListSecondHalf = tagListComplete.subList(2, 4);

        // make a team
        Team team1 = createTeam(client);

        // associate half of the tags to this team
        Set<Tag> tagSet = new HashSet<>();
        tagSet.addAll(tagListFirstHalf);
        teamTagService.save(team1, tagSet);

        Page<TeamTag> tags = teamTagService.findAllTeamTagsWithTeam(null, team1);
        List<Tag> savedTags = new ArrayList<>();
        for (TeamTag tag : tags) {
            savedTags.add(tag.getTag());
        }
        assertThat("tags should be found", savedTags, hasItems(tagListFirstHalf.toArray(new Tag[tagListFirstHalf.size()])));

        // check that new tag save, overwrites the first save
        Set<Tag> overwriteTags = new HashSet<>();
        overwriteTags.addAll(tagListSecondHalf);
        teamTagService.save(team1, overwriteTags);

        // load the tags again
        savedTags = new ArrayList<>();
        tags = teamTagService.findAllTeamTagsWithTeam(null, team1);
        for (TeamTag tag : tags) {
            savedTags.add(tag.getTag());
        }
        assertThat("first set should not be found", savedTags, not(hasItems(tagListFirstHalf.toArray(new Tag[tagListFirstHalf.size()]))));
        assertThat("second set should be found", savedTags, hasItems(tagListSecondHalf.toArray(new Tag[tagListSecondHalf.size()])));
    }

    /**
     * Test saveSingleTeamTag
     */
    @Test
    public void testSaveSingleTeamTag() {
        Client client = createClient();
        Group group = createGroup(client);
        List<Tag> tagList = createTagList(group, 1);
        Tag tag = tagList.get(0);
        Team team1 = createTeam(client);

        TeamTag teamTag = teamTagService.saveSingleTeamTag(team1, tag);
        TeamTag newTeamTag = teamTagService.reload(teamTag);
        assertThat("teamTagPage was saved", newTeamTag.getTag().getId(), is(tag.getId()));
    }

    /**
     * Test saveSingleTeamTag with null tag
     */
    @Test
    public void testSaveSingleTeamTagWithNullTag() {
        Client client = createClient();
        Tag tag = null;
        Team team1 = createTeam(client);
        team1.setId(1l);

        assertThat("null was returned", teamTagService.saveSingleTeamTag(team1, tag), is(nullValue()));
    }

    /**
     * Test saveSingleTeamTag with null team
     */
    @Test
    public void testSaveSingleTeamTagWitNullTeam() {
        Client client = createClient();
        Group group = createGroup(client);
        List<Tag> tagList = createTagList(group, 1);
        Tag tag = tagList.get(0);
        Team team1 = null;

        teamTagService.saveSingleTeamTag(team1, tag);
    }

    /**
     * Test deleteSingleTeamTag
     */
    @Test
    public void testDeleteSingleTeamTag() {
        Client client = createClient();
        Group group = createGroup(client);
        List<Tag> tagList = createTagList(group, 1);
        Tag tag = tagList.get(0);
        Team team1 = createTeam(client);

        TeamTag teamTag = teamTagService.saveSingleTeamTag(team1, tag);
        TeamTag newTeamTag = teamTagService.reload(teamTag);
        assertThat("teamTagPage was saved", newTeamTag.getTag().getId(), is(tag.getId()));

        teamTagService.deleteSingleTeamTag(newTeamTag);
        teamTag = teamTagService.reload(newTeamTag);
        assertThat("teamTag was deleted", teamTag, is(nullValue()));
    }


    /**
     * Save success
     */
    @Test
    public void save() {
        Client client = createClient();
        Group group = createGroup(client);

        Tag tag = createTagList(group,1).get(0);
        Team team = createTeam(client);
        tag = tagService.reload(tag);

        TeamTag afterSaveTeamTag = teamTagService.saveSingleTeamTag(team, tag);
        assertThat("TeamTag was given an id", afterSaveTeamTag.getId(), is(notNullValue()));
        assertThat("system is the created by", afterSaveTeamTag.getCreatedBy(), is("system"));

        TeamTagSearchFilter searchFilter = new TeamTagSearchFilter();
        HashSet<Tag> tagSet= new HashSet<>();
        tagSet.add(tag);
        searchFilter.setTagSet(tagSet);
        Page<TeamTag> teamsWithTag = teamTagService.findTeamsWithTag(null, searchFilter);
        assertThat("we can find the team tag by the team tag id",
                teamsWithTag,
                hasItem(afterSaveTeamTag));
    }

    /**
     * Save success
     */
    @Test
    public void saveTagsInSameGroup() {
        Client client = createClient();
        Group group1 = createGroup(client);
        Group group2 = createGroup(client);

        List<Tag> tagList = createTagList(group1, 2);
        List<Tag> tagList2 = createTagList(group2, 1);
        Tag tag1 = tagList.get(0);
        Tag tag2 = tagList.get(1);
        Tag tag3 = tagList2.get(0);

        Team team1 = createTeam(client);
        Team team2 = createTeam(client);
        Team team3 = createTeam(client);

        TeamTag afterSaveTeamTag1 = teamTagService.saveSingleTeamTag(team1, tag1);
        TeamTag afterSaveTeamTag2 = teamTagService.saveSingleTeamTag(team2, tag2);
        TeamTag afterSaveTeamTag3 = teamTagService.saveSingleTeamTag(team3, tag3);

        TeamTagSearchFilter searchFilter = new TeamTagSearchFilter();
        HashSet<Tag> tagSet = new HashSet<>();
        tagSet.add(tag1);
        tagSet.add(tag2);
        searchFilter.setTagSet(tagSet);
        Page<TeamTag> returnedTeamTags = teamTagService.findTeamsWithTag(null, searchFilter);
        assertThat("afterSaveTeamTag1 team is returned", returnedTeamTags, hasItem(afterSaveTeamTag1));
        assertThat("afterSaveTeamTag2 team is returned",returnedTeamTags,hasItem(afterSaveTeamTag2));
        assertThat("afterSaveTeamTag3 team is not returned",returnedTeamTags,not(hasItem(afterSaveTeamTag3)));
    }

    /**
     * Save success
     */
    @Test
    public void saveTagsInDifferentGroup() {
        Client client = createClient();
        Group group1 = createGroup(client);
        Group group2 = createGroup(client);

        List<Tag> tagList = createTagList(group1, 2);
        List<Tag> tagList2 = createTagList(group2, 1);
        Tag tag1 = tagList.get(0);
        Tag tag2 = tagList.get(1);
        Tag tag3 = tagList2.get(0);

        Team team1 = createTeam(client);
        Team team2 = createTeam(client);
        Team team3 = createTeam(client);

        TeamTag afterSaveTeamTag1 = teamTagService.saveSingleTeamTag(team1, tag1);
        TeamTag afterSaveTeamTag2 = teamTagService.saveSingleTeamTag(team1, tag3);
        TeamTag afterSaveTeamTag3 = teamTagService.saveSingleTeamTag(team2, tag2);
        TeamTag afterSaveTeamTag4 = teamTagService.saveSingleTeamTag(team3, tag3);

        TeamTagSearchFilter searchFilter = new TeamTagSearchFilter();
        HashSet<Tag> tagSet = new HashSet<>();
        tagSet.add(tag1);
        tagSet.add(tag2);
        tagSet.add(tag3);
        searchFilter.setTagSet(tagSet);
        Page<TeamTag> returnedTeamTags = teamTagService.findTeamsWithTag(null, searchFilter);
        assertThat("afterSaveTeamTag1 team is returned", returnedTeamTags, hasItem(afterSaveTeamTag1));
        assertThat("afterSaveTeamTag2 team is returned", returnedTeamTags, hasItem(afterSaveTeamTag2));
        assertThat("afterSaveTeamTag3 team is not returned",returnedTeamTags,not(hasItem(afterSaveTeamTag3)));
        assertThat("afterSaveTeamTag4 team is not returned",returnedTeamTags,not(hasItem(afterSaveTeamTag4)));
    }

    /**
     * return teams with no teamtags
     */
    @Test
    public void getTeamsWithNoTeamTags() {
        Client client = createClient();
        Group group1 = createGroup(client);

        List<Tag> tagList = createTagList(group1, 2);
        Tag tag1 = tagList.get(0);

        Team team1 = createTeam(client);
        Team team2 = createTeam(client);

        teamTagService.saveSingleTeamTag(team1, tag1);

        Page<Team> returnedTeams = teamTagService.findTeamsWithNoTeamTags(null, client.getId(), "ACTIVE_ONLY" );
        assertThat("team2 is returned", returnedTeams, hasItem(team2));
        assertThat("team1 is not returned", returnedTeams, not(hasItem(team1)));
    }

}
