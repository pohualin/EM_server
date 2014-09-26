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
        User user = userService.save(new User("login","pw"));
        Client client = new Client();
        client.setTier(ClientTier.THREE);
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(ClientRegion.NORTHEAST);
        client.setName("Test Client");
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
        Client client = clientService.create(createClient());
        Group group = groupService.save(createGroup(client));

        Tag tag1 = createTag(group, "1");
        Tag tag2 = createTag(group,"2");
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag1);
        tagList.add(tag2);
        tagList = tagService.saveAllTagsForGroup(tagList,group);

        Team team1 = teamService.create(createTeam(client, 1));

        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tagList.get(0));
        tagSet.add(tagList.get(1));

        teamTagService.save(team1, tagSet);

        Page<TeamTag> teamTagPage = teamTagService.findAllTeamTagsWithTeam(null, team1);
        assertThat("teamTagPage has two teams", teamTagPage.getTotalElements(), is(2L));
    }


}
