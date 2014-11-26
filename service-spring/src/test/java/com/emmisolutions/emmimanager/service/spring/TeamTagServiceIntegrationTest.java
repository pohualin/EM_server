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
        UserAdmin user = userService.save(new UserAdmin("login " + RandomStringUtils.randomAlphanumeric(18), "pw"));

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
        return client;
    }

    private Group createGroup(Client client) {
        Group group = new Group();
        group.setName("Test Group");
        group.setClient(clientService.reload(client));
        return group;
    }

    private Tag createTag(Group group) {
        Tag tag = new Tag();
        tag.setName("Test Tag " + RandomStringUtils.randomAlphanumeric(18));
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

        return team;
    }

    /**
     * Test List tags by group id
     */
    @Test
    public void testSaveAndFind() {
        Client client = clientService.create(createClient());
        Group group = groupService.save(createGroup(client));

        List<Tag> tagList = createTagList(group, 2);

        Team team1 = teamService.create(createTeam(client));

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
        Client client = clientService.create(createClient());
        Group group = groupService.save(createGroup(client));

        // create 4 tags in a group
        List<Tag> tagListComplete = createTagList(group, 4);

        // split into two groups
        List<Tag> tagListFirstHalf = tagListComplete.subList(0, 2);
        List<Tag> tagListSecondHalf = tagListComplete.subList(2, 4);

        // make a team
        Team team1 = teamService.create(createTeam(client));

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
        Client client = clientService.create(createClient());
        Group group = groupService.save(createGroup(client));
        List<Tag> tagList = createTagList(group, 1);
        Tag tag = tagList.get(0);
        Team team1 = teamService.create(createTeam(client));

        TeamTag teamTag = teamTagService.saveSingleTeamTag(team1, tag);
        TeamTag newTeamTag = teamTagService.reload(teamTag);
        assertThat("teamTagPage was saved", newTeamTag.getTag().getId(), is(tag.getId()));
    }

    /**
     * Test saveSingleTeamTag with null tag
     */
    @Test
    public void testSaveSingleTeamTagWithNullTag() {
        Client client = clientService.create(createClient());
        Tag tag = null;
        Team team1 = teamService.create(createTeam(client));
        team1.setId(1l);

        assertThat("null was returned", teamTagService.saveSingleTeamTag(team1, tag), is(nullValue()));
    }

    /**
     * Test saveSingleTeamTag with null team
     */
    @Test
    public void testSaveSingleTeamTagWitNullTeam() {
        Client client = clientService.create(createClient());
        Group group = groupService.save(createGroup(client));
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
        Client client = clientService.create(createClient());
        Group group = groupService.save(createGroup(client));
        List<Tag> tagList = createTagList(group, 1);
        Tag tag = tagList.get(0);
        Team team1 = teamService.create(createTeam(client));

        TeamTag teamTag = teamTagService.saveSingleTeamTag(team1, tag);
        TeamTag newTeamTag = teamTagService.reload(teamTag);
        assertThat("teamTagPage was saved", newTeamTag.getTag().getId(), is(tag.getId()));

        teamTagService.deleteSingleTeamTag(newTeamTag);
        teamTag = teamTagService.reload(newTeamTag);
        assertThat("teamTag was deleted", teamTag, is(nullValue()));
    }
}
