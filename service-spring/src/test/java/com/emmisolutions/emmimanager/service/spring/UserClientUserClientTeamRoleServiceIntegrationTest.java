package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.model.UserClientUserClientTeamRoleSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientUserClientTeamRoleService;

import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * An integration test that goes across a wired persistence layer as well
 */
public class UserClientUserClientTeamRoleServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientUserClientTeamRoleService userClientUserClientTeamRoleService;

    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;

    /**
     * Ensure findPossible works with and without
     * UserClientUserClientTeamRole(s)
     * 
     */
    @Test
    public void testFindPossible() {
        Client client = makeNewRandomClient();
        Team teamA = makeNewRandomTeam(client);
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);

        UserClientUserClientTeamRoleSearchFilter filter = new UserClientUserClientTeamRoleSearchFilter(
                new UserClient(userClient.getId()), null, "a", null);
        Page<UserClientUserClientTeamRole> page = userClientUserClientTeamRoleService
                .findPossible(filter, null);
        assertThat("should contain a page of UserClientUserClientTeamRole",
                page.getContent().size() > 0, is(true));

        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(teamA);
        entity.setUserClientTeamRole(userClientTeamRole);
        List<UserClientUserClientTeamRole> incoming = new ArrayList<UserClientUserClientTeamRole>();
        incoming.add(entity);
        userClientUserClientTeamRoleService.associate(incoming);

        Page<UserClientUserClientTeamRole> pageA = userClientUserClientTeamRoleService
                .findPossible(filter, null);
        assertThat("should contain a page of UserClientUserClientTeamRole",
                pageA.getContent().size() > 0, is(true));

        List<Tag> tags = makeNewRandomTags(makeNewRandomGroup(client), 2);
        List<TeamTag> teamTags = makeNewTeamTags(teamA, new HashSet<Tag>(tags));
        UserClientUserClientTeamRoleSearchFilter filterA = new UserClientUserClientTeamRoleSearchFilter(
                new UserClient(userClient.getId()), null, "a", tags.get(0));
        Page<UserClientUserClientTeamRole> pageB = userClientUserClientTeamRoleService
                .findPossible(filterA, null);
        assertThat("should contain a page of UserClientUserClientTeamRole",
                pageB.getContent().size() > 0, is(true));
    }

    /**
     * Test find list of existing UserClientUSerClientTeamRole by given
     * userClientId and teams
     */
    @Test
    public void testFindExistingByUserClientInTeams() {
        List<UserClientUserClientTeamRole> listA = userClientUserClientTeamRoleService
                .findExistingByUserClientInTeams(null, null);
        assertThat("should return empty list", listA.size() == 0, is(true));

        List<UserClientUserClientTeamRole> listB = userClientUserClientTeamRoleService
                .findExistingByUserClientInTeams(new UserClient(), null);
        assertThat("should return empty list", listB.size() == 0, is(true));

        Team team = makeNewRandomTeam(null);
        Client client = team.getClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        List<UserClientUserClientTeamRole> incoming = new ArrayList<UserClientUserClientTeamRole>();
        incoming.add(entity);
        userClientUserClientTeamRoleService.associate(incoming);
        List<Team> teams = new ArrayList<Team>();

        List<UserClientUserClientTeamRole> listC = userClientUserClientTeamRoleService
                .findExistingByUserClientInTeams(userClient, null);
        assertThat("should return empty list", listC.size() == 0, is(true));

        List<UserClientUserClientTeamRole> listD = userClientUserClientTeamRoleService
                .findExistingByUserClientInTeams(userClient, teams);
        assertThat("should return empty list", listD.size() == 0, is(true));

        teams.add(team);
        List<UserClientUserClientTeamRole> listE = userClientUserClientTeamRoleService
                .findExistingByUserClientInTeams(userClient, teams);
        assertThat("should return empty list", listE.size() > 0, is(true));
    }

    /**
     * Test insert an UserClientUserClientTeamRole record
     */
    @Test
    public void testAssociate() {
        Client client = makeNewRandomClient();
        Team teamA = makeNewRandomTeam(client);
        Team teamB = makeNewRandomTeam(client);
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);

        List<UserClientUserClientTeamRole> incoming = new ArrayList<UserClientUserClientTeamRole>();
        UserClientUserClientTeamRole userClientUserClientTeamRoleA = new UserClientUserClientTeamRole();
        userClientUserClientTeamRoleA.setUserClient(userClient);
        userClientUserClientTeamRoleA.setUserClientTeamRole(userClientTeamRole);
        userClientUserClientTeamRoleA.setTeam(teamA);
        incoming.add(userClientUserClientTeamRoleA);

        UserClientUserClientTeamRole userClientUserClientTeamRoleB = new UserClientUserClientTeamRole();
        userClientUserClientTeamRoleB.setUserClient(userClient);
        userClientUserClientTeamRoleB.setUserClientTeamRole(userClientTeamRole);
        userClientUserClientTeamRoleB.setTeam(teamB);
        incoming.add(userClientUserClientTeamRoleB);

        Set<UserClientUserClientTeamRole> added = userClientUserClientTeamRoleService
                .associate(incoming);
        assertThat("Should contain added UserClientUserClientTeamRole",
                added.size() > 0, is(true));
    }

    /**
     * Test reload an UserClientUserClientTeamRole record
     */
    @Test
    public void testReload() {
        Team team = makeNewRandomTeam(null);
        Client client = team.getClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        List<UserClientUserClientTeamRole> incoming = new ArrayList<UserClientUserClientTeamRole>();
        incoming.add(entity);
        Set<UserClientUserClientTeamRole> added = userClientUserClientTeamRoleService
                .associate(incoming);
        List<UserClientUserClientTeamRole> addedList = new ArrayList<UserClientUserClientTeamRole>();
        addedList.addAll(added);

        UserClientUserClientTeamRole first = addedList.get(0);
        UserClientUserClientTeamRole reloaded = userClientUserClientTeamRoleService
                .reload(new UserClientUserClientTeamRole(first.getId()));
        assertThat("should be the same entity",
                first.getId() == reloaded.getId(), is(true));
    }

    /**
     * Test bad reload with null userClientUserClientTeamRoleId
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadReload() {
        userClientUserClientTeamRoleService
                .reload(new UserClientUserClientTeamRole());
    }

    /**
     * Test delete an UserClientUserClientTeamRole
     */
    @Test
    public void testDelete() {
        Team team = makeNewRandomTeam(null);
        Client client = team.getClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        List<UserClientUserClientTeamRole> incoming = new ArrayList<UserClientUserClientTeamRole>();
        incoming.add(entity);
        Set<UserClientUserClientTeamRole> added = userClientUserClientTeamRoleService
                .associate(incoming);
        List<UserClientUserClientTeamRole> addedList = new ArrayList<UserClientUserClientTeamRole>();
        addedList.addAll(added);

        UserClientUserClientTeamRole first = addedList.get(0);
        userClientUserClientTeamRoleService.delete(first);
        UserClientUserClientTeamRole reloaded = userClientUserClientTeamRoleService
                .reload(new UserClientUserClientTeamRole(first.getId()));
        assertThat("should return nothing", reloaded == null, is(true));
    }

    /**
     * Test bad delete with null userClientUserClientTeamRoleId
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadDelete() {
        userClientUserClientTeamRoleService
                .delete(new UserClientUserClientTeamRole());
    }

    /**
     * Test bad delete with null userClientId and userClientTeamRoleId
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testAnotherBadDelete() {
        userClientUserClientTeamRoleService.delete(new UserClient(),
                new UserClientTeamRole());
    }

    /**
     * Test find a page of UserClientUserClientTeamRole with userClientId and
     * userClientTeamRoleId
     */
    @Test
    public void testFindByUserClientAndUserClientTeamRole() {
        Team team = makeNewRandomTeam(null);
        Client client = team.getClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        List<UserClientUserClientTeamRole> incoming = new ArrayList<UserClientUserClientTeamRole>();
        incoming.add(entity);
        Set<UserClientUserClientTeamRole> added = userClientUserClientTeamRoleService
                .associate(incoming);

        Page<UserClientUserClientTeamRole> found = userClientUserClientTeamRoleService
                .findByUserClientAndUserClientTeamRole(userClient,
                        userClientTeamRole, null);
        assertThat("should return a page with content", found.hasContent(),
                is(true));

        Page<UserClientUserClientTeamRole> foundA = userClientUserClientTeamRoleService
                .findByUserClientAndUserClientTeamRole(userClient,
                        userClientTeamRole, new PageRequest(0, 10));
        assertThat("should return a page with content", foundA.hasContent(),
                is(true));
    }

    /**
     * Negative test find a page of UserClientUserClientTeamRole with null
     * userClientId and userClientTeamRoleId
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadFindByUserClientAndUserClientTeamRole() {
        userClientUserClientTeamRoleService
                .findByUserClientAndUserClientTeamRole(new UserClient(),
                        new UserClientTeamRole(), null);
    }

    /**
     * Test delete all UserClientUserClientTeamRole by userClientId and
     * userClientTeamRoleId
     */
    @Test
    public void testDeleteAllByUserClientAndUserClientTeamRole() {
        Team team = makeNewRandomTeam(null);
        Client client = team.getClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        List<UserClientUserClientTeamRole> incoming = new ArrayList<UserClientUserClientTeamRole>();
        incoming.add(entity);
        Set<UserClientUserClientTeamRole> added = userClientUserClientTeamRoleService
                .associate(incoming);
        List<UserClientUserClientTeamRole> addedList = new ArrayList<UserClientUserClientTeamRole>();
        addedList.addAll(added);

        UserClientUserClientTeamRole first = addedList.get(0);
        userClientUserClientTeamRoleService.delete(
                new UserClient(userClient.getId()), new UserClientTeamRole(
                        userClientTeamRole.getId()));
        UserClientUserClientTeamRole reloaded = userClientUserClientTeamRoleService
                .reload(new UserClientUserClientTeamRole(first.getId()));
        assertThat("should return nothing", reloaded == null, is(true));
    }


    @Test
    public void testReloadUserClient(){
        Team team = makeNewRandomTeam(null);
        Client client = team.getClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClient reloadedUserClient = userDetailsService.get(userClient);
        assertThat("The reloaded User Client is not null", reloadedUserClient, is(notNullValue()));
    }
}
