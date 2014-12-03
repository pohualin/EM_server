package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserService;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

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
        userService.save(aUser);

        UserDetails details = userDetailsService.loadUserByUsername(login);
        assertThat("A UserDetails object should be returned", details, is(notNullValue()));
    }
}
