package com.emmisolutions.emmimanager.service.spring.security;

import java.util.HashSet;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserService;

import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test authentication/user details fetching
 */
public class UserDetailsServiceTest extends BaseIntegrationTest {
    @Resource
    UserDetailsService userDetailsService;

    @Resource
    UserService userService;

    /**
     * Load the user after they are saved
     */
    @Test
    public void testLoad() {
        String login = "aUser";
        UserAdmin aUser = new UserAdmin(login, "pw");
        aUser.setFirstName("firstName");
        aUser.setLastName("lastName");

    	UserAdminSaveRequest req = new UserAdminSaveRequest();
       	req.setUserAdmin(aUser);
       	req.setRoles(new HashSet<UserAdminRole>());
        userService.save(req);
        
        UserDetails details = userDetailsService.loadUserByUsername(login);
        assertThat("A UserDetails object should be returned", details, is(notNullValue()));
    }

    /**
     * Bad username
     */
    @Test(expected = UsernameNotFoundException.class)
    public void badUsername(){
        userDetailsService.loadUserByUsername("$%$%");
    }

    /**
     * Test that super_admin is loaded
     */
    @Test
    public void adminUser(){
        assertThat("user admin is loaded",
            userDetailsService.loadUserByUsername("super_admin"),
            is(notNullValue()));
    }

}
