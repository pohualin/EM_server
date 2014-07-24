package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Permission;
import com.emmisolutions.emmimanager.model.PermissionName;
import com.emmisolutions.emmimanager.model.Role;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration test sample.
 */
public class UserPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserPersistence userPersistence;

    @Resource
    UserRepository userRepository;

    @Test(expected = ConstraintViolationException.class)
    public void testConstraints() {
        // user is invalid without a login
        userPersistence.saveOrUpdate(new User());
    }

    @Test
    public void testCreate() {
        User user = new User("login", "pw");
        user = userPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        User user1 = userRepository.findOne(user.getId());
        assertThat("the users saved should be the same as the user fetched", user, is(user1));
    }

    @Test
    public void testLoad(){
        String login = "aLogin";
        User user = new User(login, "pw");
        user = userPersistence.saveOrUpdate(user);
        assertThat("the user should get an id after persistence", user.getId(), is(notNullValue()));
        assertThat("the user should get a version after persistence",user.getVersion(), is(notNullValue()));

        user = userPersistence.reload(login);
        assertThat("user is found", user, is(notNullValue()));
        assertThat("username should be lowercase only", user.getLogin(), is(login.toLowerCase()));
    }

    @Test
    public void proveExistenceOfGod(){
        User god = userPersistence.fetchUserWillFullPermissions("super_admin");
        assertThat("Should have one role ", god.getRoles().size(), is(1));
        Role role = god.getRoles().iterator().next();
        assertThat("Role is the system role", role.getName(), is("SYSTEM"));

        Permission godPermission = new Permission();
        godPermission.setName(PermissionName.PERM_GOD);
        assertThat("Role has the god permission", role.getPermissions(), hasItem(godPermission));
    }

}
