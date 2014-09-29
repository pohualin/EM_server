package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.*;
import com.emmisolutions.emmimanager.persistence.repo.TagRepository;
import com.emmisolutions.emmimanager.persistence.repo.TeamTagRepository;
import com.emmisolutions.emmimanager.service.*;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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


    private Client createClient(String uniqueId) {
        User user = userService.save(new User("login "+uniqueId,"pw"));

        Client client = new Client();
        client.setTier(ClientTier.THREE);
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(ClientRegion.NORTHEAST);
        client.setName("Test Client "+uniqueId);
        client.setType(ClientType.PROVIDER);
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

    private Tag createTag(Group group, String uniqueId ) {
        Tag tag = new Tag();
        tag.setName("Test Tag "+ uniqueId);
        tag.setGroup(group);
        return tag;
    }

    private Team createTeam(Client client, int i) {
        Team team = new Team();
        team.setName("Test Team"+i);
        team.setDescription("Test Team description");
        team.setActive(i % 2 == 0);
        team.setPhone("1111111111");
        team.setFax("2222222222");
        team.setClient(client);
        return team;
    }

    /**
     * 	Test List tags by group id
     */
    @Test
    public void testSaveAndFind(){
        Client client = clientService.create(createClient("1"));
        Group group = groupService.save(createGroup(client));

        List<Tag> tagList = createTagList(group,2);

        Team team1 = teamService.create(createTeam(client, 1));

        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tagList.get(0));
        tagSet.add(tagList.get(1));

        teamTagService.save(team1, tagSet);

        Page<TeamTag> teamTagPage = teamTagService.findAllTeamTagsWithTeam(null, team1);
        assertThat("teamTagPage has two teams", teamTagPage.getTotalElements(), is(2L));
    }

    private List<Tag> createTagList(Group group, int numberOfTags) {
        List<Tag> tagList = new ArrayList<>();
        for(int i=0;i<numberOfTags;i++) {
            Tag tag = createTag(group, Integer.toString(i));
            tagList.add(tag);
        }

        tagList = tagService.saveAllTagsForGroup(tagList,group);
        return tagList;
    }

    /**
     * 	Test the save method deletes items in between saves
     */
    @Test
    public void testSaveDeletesItems(){
        Client client = clientService.create(createClient("2"));
        Group group = groupService.save(createGroup(client));

        List<Tag> tagListComplete = createTagList(group,4);
        List<Tag> tagListFirstHalf = tagListComplete.subList(0,2);
        List<Tag> tagListSecondHalf = tagListComplete.subList(2,4);

        tagService.saveAllTagsForGroup(tagListFirstHalf,group);

        Team team1 = teamService.create(createTeam(client, 1));

        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tagListFirstHalf.get(0));
        tagSet.add(tagListFirstHalf.get(1));

        teamTagService.save(team1, tagSet);

        Page<TeamTag> teamTagPage = teamTagService.findAllTeamTagsWithTeam(null, team1);
        assertThat("teamTagPage has two teams", teamTagPage.getTotalElements(), is(2L));
        assertTrue("teamTagPage should have correct 1st team",
                teamTagPage.getContent().get(0).equals(new TeamTag(team1, tagListFirstHalf.get(0))));
        assertTrue("teamTagPage should have correct 2nd team",
                teamTagPage.getContent().get(1).equals(new TeamTag(team1, tagListFirstHalf.get(1))));

        tagService.saveAllTagsForGroup(tagListSecondHalf,group);

        Set<Tag> tagSet2 = new HashSet<>();
        tagSet2.add(tagListSecondHalf.get(0));
        tagSet2.add(tagListSecondHalf.get(1));

        teamTagService.save(team1, tagSet2);

        Page<TeamTag> teamTagPage2 = teamTagService.findAllTeamTagsWithTeam(null, team1);
        System.out.println(teamTagPage2.getContent().get(0).getTeam().getName());
        System.out.println(team1.getName());
        System.out.println(teamTagPage2.getContent().get(0).getTag().getName());
        System.out.println(tagListSecondHalf.get(0).getName());
        assertThat("teamTagPage2 has two teams", teamTagPage2.getTotalElements(), is(2L));
        assertTrue("teamTagPage2 should have correct 1st team",
                teamTagPage2.getContent().get(0).equals(new TeamTag(team1, tagListSecondHalf.get(0))));
        assertTrue("teamTagPage2 should have correct 2nd team",
                teamTagPage2.getContent().get(1).equals(new TeamTag(team1, tagListSecondHalf.get(1))));

    }


}
