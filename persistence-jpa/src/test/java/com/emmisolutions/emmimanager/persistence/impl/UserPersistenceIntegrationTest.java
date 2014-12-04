package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermission;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRepository;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration test for user persistence.
 */
public class UserPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserPersistence userPersistence;

    @Resource
    UserAdminRepository userAdminRepository;

    /**
     * Invalid user no login
     */
    @Test(expected = ConstraintViolationException.class)
    public void testConstraints() {
        // user is invalid without a login
        userPersistence.saveOrUpdate(new UserAdmin());
    }

    /**
     * valid create
     */
    @Test
    public void testCreate() {
        login("some user");
        UserAdmin user = new UserAdmin("login", "pw");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user = userPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        UserAdmin user1 = userAdminRepository.findOne(user.getId());
        assertThat("the users saved should be the same as the user fetched", user, is(user1));
        assertThat("auditor is set properly", user.getCreatedBy(), is("some user"));
        logout();

    }

    /**
     * save then reloadLocationUsingClient
     */
    @Test
    public void testLoad() {
        String login = "aLogin";
        UserAdmin user = new UserAdmin(login, "pw");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user = userPersistence.saveOrUpdate(user);
        assertThat("the user should get an id after persistence", user.getId(), is(notNullValue()));
        assertThat("the user should get a version after persistence", user.getVersion(), is(notNullValue()));

        user = userPersistence.reload(login);
        assertThat("user is found", user, is(notNullValue()));
        assertThat("username should be lowercase only", user.getLogin(), is(login.toLowerCase()));
    }

    /**
     * make sure super_admin has PERM_GOD
     */
    @Test
    public void proveExistenceOfGod() {
        UserAdmin god = userPersistence.fetchUserWillFullPermissions("super_admin");
        assertThat("Should have one role ", god.getRoles().size(), is(1));
        UserAdminRole role = god.getRoles().iterator().next().getUserAdminRole();
        assertThat("Role is the system role", role.getName(), is("SYSTEM"));

        UserAdminPermission godPermission = new UserAdminPermission();
        godPermission.setName(UserAdminPermissionName.PERM_GOD);
        assertThat("Role has the god permission", role.getPermissions(), hasItem(godPermission));
    }

    /**
     * list contract owner data
     */
    @Test
    public void listPotentialContractOwners() {
        UserAdmin contractOwner = userPersistence.reload("contract_owner");
        Page<UserAdmin> ret = userPersistence.listPotentialContractOwners(null);
        assertThat("Users should be returned", ret.hasContent(), is(true));
        assertThat("contract_owner should be in the page", ret.getContent(), hasItem(contractOwner));
    }

}
