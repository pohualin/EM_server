package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.exception.IpAddressAuthenticationException;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.*;
import com.emmisolutions.emmimanager.service.audit.HttpProxyAwareAuthenticationDetails;
import com.emmisolutions.emmimanager.service.security.UserDetailsConfigurableAuthenticationProvider;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;

import static com.emmisolutions.emmimanager.service.audit.HttpProxyAwareAuthenticationDetails.RANGES.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests the authentication provider
 */
public class LegacyAuthenticationProviderTest extends BaseIntegrationTest {

    @Resource
    UserDetailsConfigurableAuthenticationProvider authenticationProvider;

    @Resource
    UserDetailsConfigurableAuthenticationProvider adminAuthenticationProvider;

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserAdminPersistence userAdminPersistence;

    @Resource
    UserClientRoleService userClientRoleService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;

    @Resource(name = "adminUserDetailsService")
    UserDetailsService adminUserDetailsService;

    @Resource
    IpRestrictConfigurationService ipRestrictConfigurationService;

    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;

    @Resource
    UserClientService userClientService;

    @PostConstruct
    private void init() {
        authenticationProvider.setUserDetailsService(userDetailsService);
        adminAuthenticationProvider.setUserDetailsService(adminUserDetailsService);
    }

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
        UserAdmin savedUserAdmin = userAdminPersistence.saveOrUpdate(userAdmin);
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
        UserAdmin savedUserAdmin = userAdminPersistence.saveOrUpdate(userAdmin);
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(savedUserAdmin.getLogin(), null));
    }

    /**
     * Test all permutations of login when ip restrictions are enabled
     */
    @Test
    public void ipAddresses() {
        String plainTextPassword = "EmmiSuperAdmin";
        UserClient userClient = makeNewRandomUserClient(makeNewRandomClient());
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        userClient.setPassword(encodedPassword.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        userClient.setSalt(encodedPassword.substring(LegacyPasswordEncoder.PASSWORD_SIZE));
        final UserClient userWithPassword = userClientPersistence.saveOrUpdate(userClient);

        // set up client restriction
        ClientRestrictConfiguration clientRestrictConfiguration = new ClientRestrictConfiguration();
        clientRestrictConfiguration.setClient(userWithPassword.getClient());
        ClientRestrictConfiguration savedRestriction = clientRestrictConfigurationService.create(clientRestrictConfiguration);

        // login when restriction disabled
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userWithPassword.getLogin(), plainTextPassword);
        authenticationToken.setDetails(new HttpProxyAwareAuthenticationDetails() {
            @Override
            public String getIp() {
                return null;
            }

            @Override
            public RANGES checkBoundaries(String lowerBoundary, String upperBoundary) {
                return OUT_OF_BOUNDS;
            }
        });
        assertThat("ip restriction disabled login works",
                authenticationProvider.authenticate(authenticationToken).isAuthenticated(), is(true));

        // enable restrictions
        savedRestriction.setIpConfigRestrict(true);
        clientRestrictConfigurationService.update(savedRestriction);

        // login when restrictions enabled, no ip ranges in client config
        authenticationToken =
                new UsernamePasswordAuthenticationToken(userWithPassword.getLogin(), plainTextPassword);
        authenticationToken.setDetails(new HttpProxyAwareAuthenticationDetails() {
            @Override
            public String getIp() {
                return null;
            }

            @Override
            public RANGES checkBoundaries(String lowerBoundary, String upperBoundary) {
                return OUT_OF_BOUNDS;
            }
        });
        assertThat("no ip ranges configured should authenticate",
                authenticationProvider.authenticate(authenticationToken).isAuthenticated(), is(true));

        // add a range
        IpRestrictConfiguration config = new IpRestrictConfiguration();
        config.setClient(userWithPassword.getClient());
        config.setIpRangeStart("10.10.10.10");
        config.setIpRangeEnd("10.10.10.10");
        ipRestrictConfigurationService.create(config);

        // login with valid ip address
        authenticationToken =
                new UsernamePasswordAuthenticationToken(userWithPassword.getLogin(), plainTextPassword);
        authenticationToken.setDetails(new HttpProxyAwareAuthenticationDetails() {
            @Override
            public String getIp() {
                return null;
            }

            @Override
            public RANGES checkBoundaries(String lowerBoundary, String upperBoundary) {
                return IN_RANGE;
            }
        });
        assertThat("valid ip should authenticate",
                authenticationProvider.authenticate(authenticationToken).isAuthenticated(), is(true));

        // login with out of bounds ip address
        authenticationToken =
                new UsernamePasswordAuthenticationToken(userWithPassword.getLogin(), plainTextPassword);
        authenticationToken.setDetails(new HttpProxyAwareAuthenticationDetails() {
            @Override
            public String getIp() {
                return null;
            }

            @Override
            public RANGES checkBoundaries(String lowerBoundary, String upperBoundary) {
                return OUT_OF_BOUNDS;
            }
        });
        try {
            authenticationProvider.authenticate(authenticationToken);
            fail("IpAddressAuthenticationException should be thrown");
        } catch (IpAddressAuthenticationException e) {
            // no-op
        }

        // login with an invalid range configured
        authenticationToken =
                new UsernamePasswordAuthenticationToken(userWithPassword.getLogin(), plainTextPassword);
        authenticationToken.setDetails(new HttpProxyAwareAuthenticationDetails() {
            @Override
            public String getIp() {
                return null;
            }

            @Override
            public RANGES checkBoundaries(String lowerBoundary, String upperBoundary) {
                return INVALID_RANGE;
            }
        });
        assertThat("invalid range ip should authenticate",
                authenticationProvider.authenticate(authenticationToken).isAuthenticated(), is(true));

        // login when no ip address can be found
        authenticationToken =
                new UsernamePasswordAuthenticationToken(userWithPassword.getLogin(), plainTextPassword);
        authenticationToken.setDetails(new HttpProxyAwareAuthenticationDetails() {
            @Override
            public String getIp() {
                return null;
            }

            @Override
            public RANGES checkBoundaries(String lowerBoundary, String upperBoundary) {
                return NO_IP;
            }
        });
        assertThat("no ip address range should authenticate",
                authenticationProvider.authenticate(authenticationToken).isAuthenticated(), is(true));

    }

    /**
     * Not found user names will actually throw UsernameNotFoundException
     * but due to the hiding of missing user names (on purpose for security reasons)
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
        UserAdmin savedUserAdmin = userAdminPersistence.saveOrUpdate(userAdmin);

        // authenticate the super user
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(savedUserAdmin.getLogin(), plainTextPassword);
        Authentication auth = adminAuthenticationProvider.authenticate(token);
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
        userClientPersistence.saveOrUpdate(userClient);

        // do a login
        UserClient loggedInUser = (UserClient) login(userClient.getLogin(), plainTextPassword);
        logout();

        // check to see that the permissions and granted authorities are present for the client and team
        assertThat("client user has been granted team level user permission",
                Collections.unmodifiableCollection(loggedInUser.getAuthorities()),
                hasItem(new SimpleGrantedAuthority(String.format("%s_%s_%s",
                        UserClientTeamPermissionName.PERM_CLIENT_TEAM_SCHEDULE_PROGRAM.toString()
                        , loggedInUser.getTeamRoles().iterator().next().getTeam().getId(),
                        loggedInUser.getClient().getId()))));
    }
}
