package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.configuration.ImpersonationHolder;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserAdminService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Test authentication/user details fetching
 */
public class UserDetailsServiceTest extends BaseIntegrationTest {

    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;

    @Resource(name = "adminUserDetailsService")
    UserDetailsService adminUserDetailsService;

    @Resource(name = "impersonationUserDetailsService")
    UserDetailsService impersonationUserDetailsService;

    @Resource
    UserAdminService userAdminService;

    /**
     * When a user has been created and not granted any roles, they
     * should not be found by the system.
     */
    @Test(expected = UsernameNotFoundException.class)
    public void noRoles() {
        String login = RandomStringUtils.randomAlphabetic(10);
        UserAdmin aUser = new UserAdmin(login, "pw");
        aUser.setFirstName("firstName");
        aUser.setLastName("lastName");

        UserAdminSaveRequest req = new UserAdminSaveRequest();
        req.setUserAdmin(aUser);
        req.setRoles(new HashSet<UserAdminRole>());
        userAdminService.save(req);

        // loading a user without any roles should throw the exception
        userDetailsService.loadUserByUsername(login);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void noRolesAdmin() {
        String login = RandomStringUtils.randomAlphabetic(10);
        UserAdmin aUser = new UserAdmin(login, "pw");
        aUser.setFirstName("firstName");
        aUser.setLastName("lastName");

        UserAdminSaveRequest req = new UserAdminSaveRequest();
        req.setUserAdmin(aUser);
        req.setRoles(new HashSet<UserAdminRole>());
        userAdminService.save(req);

        // loading a user without any roles should throw the exception
        adminUserDetailsService.loadUserByUsername(login);
    }

    /**
     * Load the user after they are saved
     */
    @Test
    @SuppressWarnings("unchecked")
    public void success() {
        UserAdmin aUser = makeNewRandomUserAdmin();
        try {
            userDetailsService.loadUserByUsername(aUser.getUsername());
            fail("admin users should not be loaded via the client facing user details");
        } catch (UsernameNotFoundException e) {
            // no - op
        }

        UserDetails adminDetails = adminUserDetailsService.loadUserByUsername(aUser.getUsername());
        assertThat("A UserDetails object should be returned using the admin interface as well", adminDetails,
                is(notNullValue()));


        // validate impersonation
        try {
            impersonationUserDetailsService.loadUserByUsername(aUser.getUsername());
            fail("didn't set the client.. impersonation should have failed");
        } catch (UsernameNotFoundException e) {
            // no - op
        }
        final Client client = makeNewRandomClient();
        final Team team = makeNewRandomTeam(client);
        ImpersonationHolder.setClientId(client.getId());
        UserDetails impersonatedDetails = impersonationUserDetailsService.loadUserByUsername(aUser.getUsername());
        ImpersonationHolder.clear();

        assertThat("impersonated user is UserClient", impersonatedDetails, is(instanceOf(UserClient.class)));
        assertThat("impersonated user is an admin at the client",
                Collections.unmodifiableCollection(impersonatedDetails.getAuthorities()),
                hasItem(new SimpleGrantedAuthority(UserClientPermissionName.PERM_CLIENT_SUPER_USER.toString() + "_" + client.getId())));
        assertThat("impersonated user set impersonated", ((UserClient) impersonatedDetails).isImpersonated(), is(true));

        assertThat("impersonated user has the team permissions",
                Collections.unmodifiableCollection(impersonatedDetails.getAuthorities()),
                hasItem(new SimpleGrantedAuthority(UserClientTeamPermissionName.PERM_CLIENT_TEAM_SCHEDULE_PROGRAM.toString() + "_" + team.getId() + "_" + client.getId())));
    }

    /**
     * Bad username
     */
    @Test(expected = UsernameNotFoundException.class)
    public void badUsername() {
        userDetailsService.loadUserByUsername("$%$%");
    }

    /**
     * Bad username
     */
    @Test(expected = UsernameNotFoundException.class)
    public void badUsernameAdmin() {
        adminUserDetailsService.loadUserByUsername("$%$%");
    }

    /**
     * Bad username
     */
    @Test(expected = UsernameNotFoundException.class)
    public void badUsernameImpersonation() {
        impersonationUserDetailsService.loadUserByUsername("$%$%");
    }

}
