package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientRoleService;
import com.emmisolutions.emmimanager.service.UserClientService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests the authentication provider
 */
public class LegacyAuthenticationProviderTest extends BaseIntegrationTest {

    @Resource
    AuthenticationProvider authenticationProvider;

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserPersistence userPersistence;

    @Resource
    UserClientRoleService userClientRoleService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    UserClientService userClientService;

    /**
     * Make sure the password is correct
     */
    @Test(expected = BadCredentialsException.class)
    public void badPassword() {
        String plainTextPassword = "aPassword";
        UserAdmin userAdmin = makeNewRandomUserAdmin();
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        userAdmin.setPassword(encodedPassword.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        userAdmin.setSalt(encodedPassword.substring(LegacyPasswordEncoder.PASSWORD_SIZE));
        UserAdmin savedUserAdmin = userPersistence.saveOrUpdate(userAdmin);
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(savedUserAdmin.getLogin(), "notThePassword"));
    }

    /**
     * No credentials should fail
     */
    @Test(expected = BadCredentialsException.class)
    public void noPassword() {
        String plainTextPassword = "aPassword";
        UserAdmin userAdmin = makeNewRandomUserAdmin();
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        userAdmin.setPassword(encodedPassword.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        userAdmin.setSalt(encodedPassword.substring(LegacyPasswordEncoder.PASSWORD_SIZE));
        UserAdmin savedUserAdmin = userPersistence.saveOrUpdate(userAdmin);
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(savedUserAdmin.getLogin(), null));
    }

    /**
     * Not found usernames will actually throw UsernameNotFoundException
     * but due to the hiding of missing usernames (on purpose for security reasons)
     * a BadCredentialsException should be thrown
     */
    @Test(expected = BadCredentialsException.class)
    public void badUsername() {
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        RandomStringUtils.randomAlphabetic(255), "doesn't matter"));
    }

    /**
     * Should be able to login with a new admin user that has
     * been granted all admin user permissions.
     */
    @Test
    public void userAdminLogin() {
        String plainTextPassword = "aPassword";
        UserAdmin userAdmin = makeNewRandomUserAdmin();
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        userAdmin.setPassword(encodedPassword.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        userAdmin.setSalt(encodedPassword.substring(LegacyPasswordEncoder.PASSWORD_SIZE));
        UserAdmin savedUserAdmin = userPersistence.saveOrUpdate(userAdmin);

        // authenticate the super user
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(savedUserAdmin.getLogin(), plainTextPassword);
        Authentication auth = authenticationProvider.authenticate(token);
        assertThat("authentication is successful",
                auth.isAuthenticated(), is(true));
        assertThat("admin user has been granted admin user permission", Collections.unmodifiableCollection(auth.getAuthorities()),
                hasItem(new SimpleGrantedAuthority(UserAdminPermissionName.PERM_ADMIN_USER.toString())));

    }

    /**
     * Create a new user client with all permissions (including team level).
     * Then authenticate as that user and make sure that the granted authorities
     * have been setup properly.
     */
    @Test
    public void userClientLogin() {
        String plainTextPassword = "EmmiSuperAdmin";
        UserClient userClient = makeNewRandomUserClient(makeNewRandomClient());
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        userClient.setPassword(encodedPassword.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        userClient.setSalt(encodedPassword.substring(LegacyPasswordEncoder.PASSWORD_SIZE));
        UserClient savedUserClient = userClientPersistence.saveOrUpdate(userClient);

        // authenticate the user
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(savedUserClient.getLogin(), plainTextPassword);
        Authentication auth = authenticationProvider.authenticate(token);
        assertThat("authentication is successful",
                auth.isAuthenticated(), is(true));

        // fetch the logged in user fully
        login(userClient.getLogin());
        UserClient oneWithAllPermissionsLoaded = userClientService.loggedIn();

        // check to see that the permissions and granted authorities match
        assertThat("client user has been granted client user permission",
                Collections.unmodifiableCollection(auth.getAuthorities()),
                hasItem(new SimpleGrantedAuthority(UserClientPermissionName.PERM_CLIENT_USER.toString())));
        assertThat("client user has been granted team level user permission",
                Collections.unmodifiableCollection(auth.getAuthorities()),
                hasItem(new SimpleGrantedAuthority(UserClientTeamPermissionName.PERM_CLIENT_TEAM_MANAGE_EMMI.toString()
                        + "_" + oneWithAllPermissionsLoaded.getTeamRoles().iterator().next().getTeam().getId())));
    }
}
