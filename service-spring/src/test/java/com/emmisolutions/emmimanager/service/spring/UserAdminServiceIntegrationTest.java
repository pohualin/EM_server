package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserAdminService;
import com.emmisolutions.emmimanager.service.spring.security.LegacyPasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * An integration test that goes across a wired persistence layer as well
 */
public class UserAdminServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserAdminService userAdminService;

    @Resource
    UserAdminPersistence userAdminPersistence;

    @Resource
    PasswordEncoder passwordEncoder;

    /**
     * Create without login
     */
    @Test(expected = ConstraintViolationException.class)
    public void testUserCreateWithoutLogin() {
        UserAdminSaveRequest req = new UserAdminSaveRequest();
        req.setUserAdmin(new UserAdmin());
        req.setRoles(new HashSet<UserAdminRole>());
        UserAdmin user = userAdminService.save(req);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
    }


    /**
     * find user admin roles without system role
     */
    @Test
    public void testUserAdminRoles() {
        Page<UserAdminRole> roles = userAdminService.listRolesWithoutSystem(null);
        assertThat("the search roles return values", roles.getContent(), is(notNullValue()));
        assertThat("the search roles return values", roles.hasContent(), is(true));
    }

    /**
     * Create with required values
     */
    @Test
    public void testUserCreate() {
        UserAdmin user = new UserAdmin(RandomStringUtils.randomAlphabetic(10), "*****");
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(10) + ".com");

        Page<UserAdminRole> roles = userAdminService.listRolesWithoutSystem(null);
        UserAdminRole role = roles.getContent().iterator().next();
        Set<UserAdminRole> adminRoles = new HashSet<>();
        adminRoles.add(role);

        UserAdminSaveRequest req = new UserAdminSaveRequest();
        req.setUserAdmin(user);
        req.setRoles(adminRoles);
        user = userAdminService.save(req);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        UserAdmin user1 = userAdminService.fetchUserWillFullPermissions(user);
        assertThat("the users saved should be the same as the user fetched", user, is(user1));
        assertThat("the users roles saved", user1.getRoles().iterator().next().getUserAdminRole(), is(role));

        user = userAdminService.save(req); //execute again just to verify that is deleting the previous roles

        UserAdminSearchFilter filter = new UserAdminSearchFilter(UserAdminSearchFilter.StatusFilter.ALL, user.getFirstName());
        Page<UserAdmin> users = userAdminService.list(null, filter);

        assertThat("the search user return values", users.getContent(), is(notNullValue()));
        assertThat("the search user return values", users.getContent(), hasItem(user));

        UserAdmin findUser = users.getContent().iterator().next();
        assertThat("the users returned is the same user saved", findUser, is(user1));

        UserAdmin newComing = new UserAdmin();
        newComing.setEmail(user.getEmail());
        List<UserAdmin> conflicts = userAdminService.findConflictingUsers(newComing);
        assertThat("conflicts contain user", conflicts, hasItem(user));
    }

    /**
     * Make sure the password mechanism works for admin users
     */
    @Test
    public void adminCanLogin() {
        UserAdmin user = new UserAdmin(RandomStringUtils.randomAlphabetic(10), "pw");
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(10) + ".com");

        Page<UserAdminRole> roles = userAdminService.listRolesWithoutSystem(null);
        UserAdminRole role = roles.getContent().iterator().next();
        Set<UserAdminRole> adminRoles = new HashSet<>();
        adminRoles.add(role);

        UserAdminSaveRequest req = new UserAdminSaveRequest();
        req.setUserAdmin(user);
        req.setRoles(adminRoles);
        user = userAdminService.save(req);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        try {
            login(user.getLogin(), "pw");
        } catch (BadCredentialsException bce) {
            String encoded = passwordEncoder.encode("pw");
            String hash = encoded.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE);
            String salt = encoded.substring(LegacyPasswordEncoder.PASSWORD_SIZE);
            user.setPassword(hash);
            user.setSalt(salt);
            userAdminPersistence.saveOrUpdate(user);
        }
        adminLogin(user.getLogin(), "pw");
        logout();
    }

    /**
     * Need to make sure that web api user's can login to the system via passwords that are
     * set via the updatePassword service
     */
    @Test
    public void webApiPasswordsWork() {
        String password = "password";
        final UserAdmin userAdmin = makeNewRandomUserAdmin();
        userAdmin.setPassword(password);

        assertThat("user is not web-api user, update password should return null",
                userAdminService.updatePassword(userAdmin), is(nullValue()));

        userAdmin.setWebApiUser(true);
        UserAdmin aWebApiUser = userAdminService.save(new UserAdminSaveRequest() {{
            setUserAdmin(userAdmin);
        }});

        aWebApiUser.setPassword(password);
        UserAdmin afterUpdate = userAdminService.updatePassword(aWebApiUser);
        assertThat("password has been changed", afterUpdate.getPassword(), is(not(password)));

        User afterLogin = adminLogin(afterUpdate.getLogin(), password);
        logout();
        assertThat("user is same", userAdmin, is(afterLogin));

    }

    /**
     * An edge case where someone loads a user that isn't in the database
     */
    @Test
    public void fetchReturnsNull(){
        assertThat("fetch full can return null", userAdminService.fetchUserWillFullPermissions(new UserAdmin()),
                is(nullValue()));
    }

    /**
     * Can't save null
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void cantSaveNull(){
        userAdminService.save(null);
    }

    /**
     * Can't save system level user
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void cantSaveSystemLevelUser(){
        userAdminService.save(new UserAdminSaveRequest(){{
            setUserAdmin(userAdminPersistence.reload("super_admin"));
        }});
    }

    /**
     * Make sure logged in user cannot change certain things about themselves
     */
    @Test
    public void loggedInUserCantChangeStuff(){
        String password = "password";
        final UserAdmin userAdmin = makeNewRandomUserAdmin();
        userAdmin.setWebApiUser(true);
        UserAdmin aWebApiUser = userAdminService.save(new UserAdminSaveRequest() {{
            setUserAdmin(userAdmin);
        }});
        aWebApiUser.setPassword(password);
        UserAdmin canLogin = userAdminService.updatePassword(aWebApiUser);

        final UserAdmin loggedIn = (UserAdmin) adminLogin(canLogin.getLogin(), password);

        // change stuff that is not allowed to be saved
        loggedIn.setWebApiUser(false);
        loggedIn.setActive(false);
        UserAdmin hackedTry = userAdminService.save(new UserAdminSaveRequest() {{
            setUserAdmin(loggedIn);
        }});

        assertThat("non allowed change to web-api user status didn't occur", hackedTry.isWebApiUser(), is(true));
        assertThat("non allowed change to active status didn't occur", hackedTry.isActive(), is(true));

        logout();
    }

}
