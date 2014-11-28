package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRoleType;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientTeamRoleService;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Roles for ClientUsers test
 */
public class UserClientTeamRoleServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientTeamRoleService userClientTeamRoleService;

    /**
     * Attempts to save incomplete roles should fail
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void createIncomplete() {
        userClientTeamRoleService.create(new UserClientTeamRole());
    }

    /**
     * Make sure we can create a role, find it and reload it
     */
    @Test
    public void createFindAndReload() {
        UserClientTeamRole userClientTeamRole = userClientTeamRoleService.create(new UserClientTeamRole("a name", makeNewRandomClient(), null));
        assertThat("new role is saved", userClientTeamRole.getId(), is(notNullValue()));
        assertThat("we can find the new role by client",
            userClientTeamRoleService.find(userClientTeamRole.getClient(), null),
            hasItem(userClientTeamRole));
        assertThat("we can reload it now", userClientTeamRoleService.reload(new UserClientTeamRole(userClientTeamRole.getId())), is(userClientTeamRole));
    }

    /**
     * Make sure possible permissions can load
     */
    @Test
    public void permissionsLoad(){
        assertThat("permissions load", userClientTeamRoleService.loadPossiblePermissions().isEmpty(), is(false));
    }

    /**
     * Invalid role creation should lead to InvalidDataAccessApiUsageException
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badCreate() {
        userClientTeamRoleService.create(null);
    }

    /**
     * Invalid role removal should lead to InvalidDataAccessApiUsageException
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badRemove() {
        userClientTeamRoleService.remove(new UserClientTeamRole());
    }

    /**
     * Invalid role searching should lead to InvalidDataAccessApiUsageException
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badFinder() {
        userClientTeamRoleService.find(new Client(), null);
    }

    /**
     * Find of a null should return a blank page
     */
    @Test
    public void blankReturnFinderCall() {
        assertThat("search using null client should yield no results", userClientTeamRoleService.find(new Client(12341234l), null).getTotalElements(), is(0l));
    }

    /**
     * This test ensures that a new client role can be saved with a set of permissions and
     * that those permissions are retrievable without an eager fetch. Also removes
     * the role and ensures that it can't be reloaded again.
     */
    @Test
    public void fullStackSave() {
        // save a role with permissions
        Set<UserClientTeamPermission> userClientTeamPermissions = new HashSet<>();
        userClientTeamPermissions.add(new UserClientTeamPermission(UserClientTeamPermissionName.PERM_CLIENT_TEAM_MODIFY_USER_ROLE));
        UserClientTeamRole userClientTeamRole = userClientTeamRoleService.create(new UserClientTeamRole("permission save", makeNewRandomClient(), userClientTeamPermissions));

        // fetch all of the permissions for the saved role using a find() which will guarantee a flush of the session
        assertThat("permission should be present after a find for the role and then a fetch for the permissions",
            userClientTeamRoleService.loadAll(userClientTeamRoleService.find(userClientTeamRole.getClient(), null).iterator().next()),
            hasItem(new UserClientTeamPermission(UserClientTeamPermissionName.PERM_CLIENT_TEAM_MODIFY_USER_ROLE)));

        userClientTeamRole.setName("updated name");
        UserClientTeamRole savedUserClientTeamRole = userClientTeamRoleService.update(userClientTeamRole);
        assertThat("updates work", savedUserClientTeamRole.getVersion(), is(not(userClientTeamRole.getVersion())));
        savedUserClientTeamRole.setType(new UserClientReferenceTeamRoleType(1l));
        assertThat("type should not change on update", userClientTeamRoleService.update(savedUserClientTeamRole).getType(), is(nullValue()));

        userClientTeamRoleService.remove(userClientTeamRole);

        assertThat("client role should have been deleted", userClientTeamRoleService.reload(userClientTeamRole), is(nullValue()));
    }
    /**
     * Make sure we can load a page
     */
    @Test
    public void load() {
        assertThat("Reference Roles are loaded",
            userClientTeamRoleService.loadReferenceRoles(null).getTotalElements(), is(not(0l)));
    }

}
