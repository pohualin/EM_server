package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.*;
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
        User user = userService.save(new User("login " + System.currentTimeMillis(), "pw"));

        Client client = new Client();
        client.setTier(ClientTier.THREE);
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(ClientRegion.NORTHEAST);
        client.setName("Test Client " + System.currentTimeMillis());
        client.setType(new ClientType(1l));
        client.setContractOwner(user);
        client.setActive(false);
        client.setSalesForceAccount(new SalesForce("xxxWW" + System.currentTimeMillis()));
        return client;
    }

    private Group createGroup(Client client) {
        Group group = new Group();
        group.setName("Test Group");
        group.setClient(clientService.reload(client));
        return group;
    }

    private Tag createTag(Group group, String uniqueId) {
        Tag tag = new Tag();
        tag.setName("Test Tag " + uniqueId);
        tag.setGroup(group);
        return tag;
    }

    private Team createTeam(Client client) {
        Team team = new Team();
        team.setName("Test Team" + System.currentTimeMillis());
        team.setDescription("Test Team description");
        team.setActive(true);
        team.setClient(client);
        team.setSalesForceAccount(new TeamSalesForce("xxxWW" + System.currentTimeMillis()));

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
            Tag tag = createTag(group, Integer.toString(i));
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
}
