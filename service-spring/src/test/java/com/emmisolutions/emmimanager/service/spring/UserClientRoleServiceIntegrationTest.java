package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRoleType;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientRoleService;
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
public class UserClientRoleServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientRoleService userClientRoleService;

    /**
     * Attempts to save incomplete roles should fail
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void createIncomplete() {
        userClientRoleService.create(new UserClientRole());
    }

    /**
     * Make sure we can create a role, find it and reload it
     */
    @Test
    public void createFindAndReload() {
        UserClientRole userClientRole = userClientRoleService.create(new UserClientRole("a name", makeNewRandomClient(), null));
        assertThat("new role is saved", userClientRole.getId(), is(notNullValue()));
        assertThat("we can find the new role by client",
            userClientRoleService.find(userClientRole.getClient(), null),
            hasItem(userClientRole));
        assertThat("we can reload it now", userClientRoleService.reload(new UserClientRole(userClientRole.getId())), is(userClientRole));
    }

    /**
     * Make sure possible permissions can load
     */
    @Test
    public void permissionsLoad(){
        assertThat("permissions load", userClientRoleService.loadPossiblePermissions().isEmpty(), is(false));
    }

    /**
     * Load reference data
     */
    @Test
    public void roleLibrary(){
        assertThat("role library loads", userClientRoleService.loadReferenceRoles(null).getTotalElements(), is(not(0l)));
    }

    /**
     * Invalid role creation should lead to InvalidDataAccessApiUsageException
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badCreate() {
        userClientRoleService.create(null);
    }

    /**
     * Invalid role removal should lead to InvalidDataAccessApiUsageException
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badRemove() {
        userClientRoleService.remove(new UserClientRole());
    }

    /**
     * Invalid role searching should lead to InvalidDataAccessApiUsageException
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badFinder() {
        userClientRoleService.find(new Client(), null);
    }

    /**
     * Find of a null should return a blank page
     */
    @Test
    public void blankReturnFinderCall() {
        assertThat("search using null client should yield no results", userClientRoleService.find(new Client(12341234l), null).getTotalElements(), is(0l));
    }

    /**
     * This test ensures that a new client role can be saved with a set of permissions and
     * that those permissions are retrievable without an eager fetch. Also removes
     * the role and ensures that it can't be reloaded again.
     */
    @Test
    public void fullStackWithPermissions() {
        // save a role with permissions
        Set<UserClientPermission> userClientPermissions = new HashSet<>();
        userClientPermissions.add(new UserClientPermission(UserClientPermissionName.PERM_CLIENT_SUPER_USER));
        UserClientRole userClientRole = userClientRoleService.create(new UserClientRole("permission save", makeNewRandomClient(), userClientPermissions));

        // fetch all of the permissions for the saved role using a find() which will guarantee a flush of the session
        assertThat("permission should be present after a find for the role and then a fetch for the permissions",
            userClientRoleService.loadAll(userClientRoleService.find(userClientRole.getClient(), null).iterator().next()),
            hasItem(new UserClientPermission(UserClientPermissionName.PERM_CLIENT_SUPER_USER)));

        userClientRole.setName("updated name");
        UserClientRole savedUserClientRole = userClientRoleService.update(userClientRole);
        assertThat("updates work", savedUserClientRole.getVersion(), is(not(userClientRole.getVersion())));
        savedUserClientRole.setType(new UserClientReferenceRoleType(1l));
        assertThat("type should not change on update", userClientRoleService.update(savedUserClientRole).getType(), is(nullValue()));

        userClientRoleService.remove(userClientRole);

        assertThat("client role should have been deleted", userClientRoleService.reload(userClientRole), is(nullValue()));
    }

    /**
     * Loading permissions on null role should return null
     */
    @Test
    public void loadPermissions(){
         assertThat("empty permissions of null client role", userClientRoleService.loadAll(null).isEmpty(), is(true));
    }

    /**
     * Ensure reference roles are loaded properly
     */
    @Test
    public void load() {
        assertThat("Reference Roles are loaded",
            userClientRoleService.loadReferenceRoles(null).getTotalElements(), is(not(0l)));
    }
    
    /**
     * Test findByNormalizedName
     */
    @Test
    public void findByNormalizedName(){
        UserClientRole first = makeNewRandomUserClientRole(null);
        Client client = first.getClient();
        
        UserClientRole second = new UserClientRole();
        second.setName(first.getName());
        second.setClient(client);
        assertThat("Should find one match", userClientRoleService.hasDuplicateName(second), is(true));
        
        UserClientRole third = new UserClientRole();
        third.setId(first.getId());
        third.setName(first.getName());
        third.setClient(client);
        assertThat("Should ignore self", userClientRoleService.hasDuplicateName(third), is(false));
    }
}
