package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRoleType;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientRolePersistence;

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test UserClientRole persistence
 */
public class UserClientRolePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientRolePersistence userClientRolePersistence;

    /**
     * Make sure incomplete save requests fail
     */
    @Test(expected = ConstraintViolationException.class)
    public void createIncomplete() {
        userClientRolePersistence.save(new UserClientRole());
    }

    /**
     * Create a UserClientRole and then find and reload it
     */
    @Test
    public void createFindAndReload() {
        UserClientRole toBeSaved = new UserClientRole("a name", makeNewRandomClient(), null);
        toBeSaved.setType(new UserClientReferenceRoleType(1l));
        UserClientRole userClientRole = userClientRolePersistence.save(toBeSaved);
        assertThat("new role is saved", userClientRole.getId(), is(notNullValue()));
        assertThat("normalized name set", userClientRole.getNormalizedName(), is("aname"));
        assertThat("we can find the new role by client",
            userClientRolePersistence.find(userClientRole.getClient().getId(), null),
            hasItem(userClientRole));
        assertThat("we can reload it now", userClientRolePersistence.reload(new UserClientRole(userClientRole.getId())), is(userClientRole));

        userClientRolePersistence.remove(userClientRole.getId());

        assertThat("should be removed", userClientRolePersistence.reload(new UserClientRole(userClientRole.getId())), is(nullValue()));
    }

    /**
     * Save of a null should fail
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void badSave(){
        userClientRolePersistence.save(null);
    }
    
    /**
     * Save should fail with duplicate name
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void badSaveWithDuplicateName(){
        UserClientRole first = makeNewRandomUserClientRole(null);
        UserClientRole second = new UserClientRole(first.getName(), first.getClient(), null);
        second.setType(new UserClientReferenceRoleType(1l));
        userClientRolePersistence.save(second);
    }

    /**
     * Load all possible permissions
     */
    @Test
    public void loadPossible(){
        assertThat("load all possible works", userClientRolePersistence.loadPossiblePermissions().isEmpty(), is(false)) ;
    }

    /**
     * Make sure permissions can be saved transparently
     */
    @Test
    public void permissionSave() {
        Set<UserClientPermission> userClientPermissions = new HashSet<>();
        userClientPermissions.add(new UserClientPermission(UserClientPermissionName.PERM_CLIENT_SUPER_USER));
        UserClientRole userClientRole = userClientRolePersistence.save(new UserClientRole("permission save", makeNewRandomClient(), userClientPermissions));
        assertThat("permission present",
            userClientRolePersistence.permissionsFor(userClientRole),
            hasItem(new UserClientPermission(UserClientPermissionName.PERM_CLIENT_SUPER_USER)));

    }

    /**
     * Reload of a null should result in null
     */
    @Test
    public void nullReload(){
        assertThat("null reload returns null", userClientRolePersistence.reload(null), is(nullValue()));
    }

    /**
     * Loading permissions on null role should return null
     */
    @Test
    public void loadPermissions(){
        assertThat("empty permissions of null client role", userClientRolePersistence.permissionsFor(new UserClientRole()).isEmpty(), is(true));
    }
    
    @Test
    public void findByNorminalizedNameAndClient() {
        UserClientRole first = makeNewRandomUserClientRole(null);
        UserClientRole toFind = new UserClientRole();
        toFind.setClient(first.getClient());
        toFind.setName(first.getName());
        assertThat("Found the one we made",
                userClientRolePersistence.findDuplicateByName(toFind),
                is(first));

        UserClientRole another = new UserClientRole();
        another.setClient(makeNewRandomClient());
        another.setName(first.getName());
        assertThat("Should not find anything",
                userClientRolePersistence.findDuplicateByName(another),
                is(nullValue()));
    }

}
