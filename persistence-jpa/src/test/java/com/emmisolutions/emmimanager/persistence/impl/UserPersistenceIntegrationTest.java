package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.UserSearchFilter;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermission;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;

/**
 * Integration test for user persistence.
 */
public class UserPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserPersistence userPersistence;

    @Resource
    UserAdminRepository userAdminRepository;

    @Resource
    UserRepository userRepository;
    
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
    
    @Test
    public void testSimpleCreate() {
    	login("some user");
        User user = new User();
        user.setLogin("claudio");
        user.setPassword("clave");
        user.setFirstName("firstName1");
        user.setLastName("lastName1");
        user.setActive(true);
        user = userPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        User user1 = userRepository.findOne(user.getId());
        assertThat("the users saved should be the same as the user fetched", user, is(user1));
        assertThat("auditor is set properly", user.getCreatedBy(), is("some user"));
        
        UserSearchFilter filter = new UserSearchFilter(UserSearchFilter.StatusFilter.ALL , "claudio");
        Page<User> users = userPersistence.list(null, filter);
        
        assertThat("the search user return values", users.getContent(), is(notNullValue()));
        assertThat("the search user return values", users.getContent().size(), is(1) );
        
        User findUser = users.getContent().iterator().next();
        assertThat("the users returned is the same user saved", findUser, is(user1));
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
