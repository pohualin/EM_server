package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientTeamRolePersistence;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration test for UserClientPersistence
 */
public class UserClientUserClientTeamRolePersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientUserClientTeamRolePersistence userClientUserClientTeamRolePersistence;

    /**
     * Test create an UserClientUserClientTeamRole and that the user client
     * can be found by using the team associated to that role.
     */
    @Test
    public void testCreate() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);

        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);

        UserClientUserClientTeamRole created = userClientUserClientTeamRolePersistence
                .saveOrUpdate(entity);
        assertThat("entity created", created.getId(), is(notNullValue()));

        UserClientSearchFilter filter = new UserClientSearchFilter();
        filter.setClient(client);
        filter.setTeam(team);
        assertThat("the user client should be found when the team is added to the filter",
                userClientPersistence.list(null, filter), hasItem(userClient));
    }

    /**
     * Test reload an UserClientUserClientTeamRole by
     * userClientUserClientTeamRoleId
     */
    @Test
    public void testReload() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        userClientUserClientTeamRolePersistence.saveOrUpdate(entity);

        assertThat("should return the existing one",
                userClientUserClientTeamRolePersistence.reload(entity.getId()),
                is(entity));
    }

    /**
     * Test delete an UserClientUserClientTeamRole by
     * userClientUserClientTeamRoleId
     */
    @Test
    public void testDelete() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        userClientUserClientTeamRolePersistence.saveOrUpdate(entity);

        userClientUserClientTeamRolePersistence.delete(entity.getId());
        UserClientUserClientTeamRole reloadAfterDelete = userClientUserClientTeamRolePersistence
                .reload(entity.getId());
        assertThat("should return nothing", reloadAfterDelete == null, is(true));
    }

    /**
     * Test find a list of UserClientUserClientTeamRole by given userClientId
     * and teams
     */
    @Test
    public void testFindByUserClientIdInTeams() {
        Team team = makeNewRandomTeam();
        Client client = team.getClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);

        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        userClientUserClientTeamRolePersistence.saveOrUpdate(entity);

        List<Team> teams = new ArrayList<>();
        teams.add(team);
        assertThat("should return a list of UserClientUserClientTeamRole",
                userClientUserClientTeamRolePersistence
                        .findByUserClientIdAndTeamsIn(userClient.getId(), teams).size() > 0, is(true));
    }

    /**
     * Test find a page of UserClientUserClientTeamRole by given userClientId
     * and userClientTeamRoleId
     */
    @Test
    public void testFindByUserClientIdAndUserClientTeamRoleId() {
        Team team = makeNewRandomTeam();
        Client client = team.getClient();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);

        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        userClientUserClientTeamRolePersistence.saveOrUpdate(entity);

        Page<UserClientUserClientTeamRole> found = userClientUserClientTeamRolePersistence
                .findByUserClientIdAndUserClientTeamRoleId(userClient.getId(),
                        userClientTeamRole.getId(), null);
        assertThat("should return a page of UserClientUserClientTeamRole",
                found.hasContent(), is(true));

        Page<UserClientUserClientTeamRole> foundA = userClientUserClientTeamRolePersistence
                .findByUserClientIdAndUserClientTeamRoleId(userClient.getId(),
                        userClientTeamRole.getId(), new PageRequest(0, 10));
        assertThat("should return a page of UserClientUserClientTeamRole",
                foundA.hasContent(), is(true));
    }

    /**
     * Test delete all UserClientUserClientTeamRole by given userClientId and
     * userClientTeamRoleId
     */
    @Test
    public void testDeleteByUserClientAndUserClientTeamRole() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam();
        UserClient userClient = makeNewRandomUserClient(client);
        UserClientTeamRole userClientTeamRole = makeNewRandomUserClientTeamRole(client);
        UserClientUserClientTeamRole entity = new UserClientUserClientTeamRole();
        entity.setUserClient(userClient);
        entity.setTeam(team);
        entity.setUserClientTeamRole(userClientTeamRole);
        userClientUserClientTeamRolePersistence.saveOrUpdate(entity);

        userClientUserClientTeamRolePersistence.delete(userClient.getId(),
                userClientTeamRole.getId());
        UserClientUserClientTeamRole reloadAfterDelete = userClientUserClientTeamRolePersistence
                .reload(entity.getId());
        assertThat("should return nothing", reloadAfterDelete == null, is(true));
    }
}
