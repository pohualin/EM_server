package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Team Service Integration test
 */
public class TeamServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientService clientService;

    @Resource
    TeamService teamService;

    @Resource
    UserService userService;

    @Resource
    GroupService groupService;

    @Resource
    TagService tagService;

    @Resource
    TeamTagService teamTagService;

    /**
     * Not all required fields
     */
    @Test(expected = ConstraintViolationException.class)
    public void createNotAllRequired() {
        Team team = new Team();
        teamService.create(team);
    }

    /**
     * Create team successfully
     */
    @Test
    public void create() {
        Client client = makeClient("clientTeam", "teamUser");
        clientService.create(client);

        Team team = makeTeamForClient(client);
        Team savedTeam = teamService.create(team);
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));

        assertThat("Can find the saved team", teamService.list(new TeamSearchFilter(client.getId(),
                        TeamSearchFilter.StatusFilter.INACTIVE_ONLY, "Test Team")),
                hasItem(savedTeam));

        assertThat("Can find the saved team via normalized name",
                teamService.findByNormalizedNameAndClientId(team.getNormalizedTeamName(), client.getId()),
                is(savedTeam));
    }

    @Test
    public void update() {
        Client client = makeClient("clientTeam2", "teamUser1");
        clientService.create(client);

        Team team = makeTeamForClient(client);
        Team savedTeam = teamService.create(team);
        assertThat("team was created successfully", savedTeam.getId(), is(notNullValue()));

        savedTeam.setClient(clientService.create(makeClient("a different client", "teamUser2")));
        savedTeam = teamService.update(team);
        assertThat("client was not updated", savedTeam.getClient(), is(client));
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

        Team team1 = createTeam(client, true);
        Team team2 = createTeam(client, true);

        teamTagService.saveSingleTeamTag(team1, tag1);

        TeamSearchFilter filter = new TeamSearchFilter(client.getId(), TeamSearchFilter.StatusFilter.ACTIVE_ONLY, TeamSearchFilter.TeamTagType.ALL);
        Page<Team> returnedTeams = teamService.list(null, filter);
        assertThat("team2 is returned", returnedTeams, hasItem(team2));
        assertThat("team1 is not returned", returnedTeams, not(hasItem(team1)));
    }

    /**
     * return inactive teams with no teamtags
     */
    @Test
    public void getInactiveTeamsWithNoTeamTags() {
        Client client = createClient();
        Group group1 = createGroup(client);

        List<Tag> tagList = createTagList(group1, 2);
        Tag tag1 = tagList.get(0);

        Team team1 = createTeam(client, true);
        Team team2 = createTeam(client, false);

        teamTagService.saveSingleTeamTag(team1, tag1);

        TeamSearchFilter filter = new TeamSearchFilter(client.getId(), TeamSearchFilter.StatusFilter.INACTIVE_ONLY,TeamSearchFilter.TeamTagType.ALL);
        Page<Team> returnedTeams = teamService.list(null, filter);
        assertThat("team2 is returned", returnedTeams, hasItem(team2));
        assertThat("team1 is not returned", returnedTeams, not(hasItem(team1)));
    }

    /**
     * return active and inactive teams with no teamtags
     */
    @Test
    public void getAllTeamsWithNoTeamTags() {
        Client client = createClient();
        Group group1 = createGroup(client);

        List<Tag> tagList = createTagList(group1, 2);
        Tag tag1 = tagList.get(0);

        Team team1 = createTeam(client, true);
        Team team2 = createTeam(client, true);
        Team team3 = createTeam(client, true);

        teamTagService.saveSingleTeamTag(team1, tag1);

        TeamSearchFilter filter = new TeamSearchFilter(client.getId(), TeamSearchFilter.StatusFilter.ALL, TeamSearchFilter.TeamTagType.ALL);
        Page<Team> returnedTeams = teamService.list(null, filter);
        assertThat("team2 is returned", returnedTeams, hasItem(team2));
        assertThat("team3 is returned", returnedTeams, hasItem(team3));
        assertThat("team1 is not returned", returnedTeams, not(hasItem(team1)));
    }

    private Team makeTeamForClient(Client client) {
        Team team = new Team();
        team.setName("Test Team");
        team.setDescription("Test Team description");
        team.setActive(false);
        team.setClient(client);
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return team;
    }

    private Team createTeam(Client client, Boolean active) {
        Team team = new Team();
        team.setName("Test Team" + RandomStringUtils.randomAlphanumeric(18));
        team.setDescription("Test Team description");
        team.setActive(active);
        team.setClient(client);
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        teamService.create(team);
        return team;
    }

    protected Client makeClient(String clientName, String username) {
        Client client = new Client();
        client.setType(new ClientType(4l));
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        client.setContractOwner(new UserAdmin(1l, 0));
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return client;
    }

    private Group createGroup(Client client) {
        Group group = new Group();
        group.setName("Test Group" + RandomStringUtils.randomAlphanumeric(9));
        group.setClient(clientService.reload(client));
        groupService.save(group);
        return group;
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

    private Tag createTag(Group group) {
        Tag tag = new Tag();
        tag.setName("Test Tag " + RandomStringUtils.randomAlphanumeric(9));
        tag.setGroup(group);
        return tag;
    }

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
}
