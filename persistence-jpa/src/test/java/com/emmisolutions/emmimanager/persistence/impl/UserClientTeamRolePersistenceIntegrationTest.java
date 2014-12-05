package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRoleType;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientTeamRolePersistence;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test UserClientTeamRole persistence
 */
public class UserClientTeamRolePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientTeamRolePersistence userClientTeamRolePersistence;

    /**
     * Make sure incomplete save requests fail
     */
    @Test(expected = ConstraintViolationException.class)
    public void createIncomplete() {
        userClientTeamRolePersistence.save(new UserClientTeamRole());
    }

    /**
     * Create a UserClientTeamRole and then find and reload it
     */
    @Test
    public void createFindAndReload() {
        UserClientTeamRole toBeSaved = new UserClientTeamRole("a name", makeNewRandomClient(), null);
        toBeSaved.setType(new UserClientReferenceTeamRoleType(1l));
        UserClientTeamRole userClientTeamRole = userClientTeamRolePersistence.save(toBeSaved);

        assertThat("new role is saved", userClientTeamRole.getId(), is(notNullValue()));
        assertThat("we can find the new role by client",
            userClientTeamRolePersistence.find(userClientTeamRole.getClient().getId(), null),
            hasItem(userClientTeamRole));
        assertThat("we can reload it now", userClientTeamRolePersistence.reload(new UserClientTeamRole(userClientTeamRole.getId())), is(userClientTeamRole));

        userClientTeamRolePersistence.remove(userClientTeamRole.getId());

        assertThat("should be removed", userClientTeamRolePersistence.reload(new UserClientTeamRole(userClientTeamRole.getId())), is(nullValue()));
    }

    /**
     * Make sure permissions can be saved transparently
     */
    @Test
    public void permissionSave() {
        Set<UserClientTeamPermission> userClientTeamPermissions = new HashSet<>();
        userClientTeamPermissions.add(new UserClientTeamPermission(UserClientTeamPermissionName.PERM_CLIENT_TEAM_MANAGE_EMMI));
        UserClientTeamRole userClientTeamRole = userClientTeamRolePersistence.save(new UserClientTeamRole("permission save", makeNewRandomClient(), userClientTeamPermissions));
        assertThat("permission present",
            userClientTeamRolePersistence.permissionsFor(userClientTeamRole),
            hasItem(new UserClientTeamPermission(UserClientTeamPermissionName.PERM_CLIENT_TEAM_MANAGE_EMMI)));
    }

    /**
     * Save of a null should fail
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badSave(){
        userClientTeamRolePersistence.save(null);
    }

    /**
     * Load all possible permissions
     */
    @Test
    public void loadPossible(){
        assertThat("load all possible works", userClientTeamRolePersistence.loadPossiblePermissions().isEmpty(), is(false)) ;
    }

    /**
     * Reload of a null should result in null
     */
    @Test
    public void nullReload(){
        assertThat("null reload returns null", userClientTeamRolePersistence.reload(null), is(nullValue()));
    }

    /**
     * Loading permissions on null role should return null
     */
    @Test
    public void loadPermissions(){
        assertThat("empty permissions of null client role", userClientTeamRolePersistence.permissionsFor(new UserClientTeamRole()).isEmpty(), is(true));
    }

}
