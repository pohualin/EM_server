package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserAdminService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
    public void success() {
        UserAdmin aUser = makeNewRandomUserAdmin();
        try {
            userDetailsService.loadUserByUsername(aUser.getUsername());
            fail("admin users should not be loaded via the client facing user details");
        } catch (UsernameNotFoundException e){
            // no - op
        }

        UserDetails adminDetails = adminUserDetailsService.loadUserByUsername(aUser.getUsername());
        assertThat("A UserDetails object should be returned using the admin interface as well", adminDetails,
                is(notNullValue()));
    }

    /**
     * Bad username
     */
    @Test(expected = UsernameNotFoundException.class)
    public void badUsername(){
        userDetailsService.loadUserByUsername("$%$%");
    }

    /**
     * Bad username
     */
    @Test(expected = UsernameNotFoundException.class)
    public void badUsernameAdmin(){
        adminUserDetailsService.loadUserByUsername("$%$%");
    }

}
