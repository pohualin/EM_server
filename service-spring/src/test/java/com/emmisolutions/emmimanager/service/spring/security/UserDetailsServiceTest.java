package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.User;
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
 * Created by matt on 7/14/14.
 */
public class UserDetailsServiceTest extends BaseIntegrationTest {
    @Resource
    UserDetailsService userDetailsService;

    @Resource
    UserService userService;


    @Test
    public void testLoad(){
        String login = "aUser";
        User aUser = new User(login, "pw");
        userService.save(aUser);

        UserDetails details = userDetailsService.loadUserByUsername(login);
        assertThat("A UserDetails object should be returned", details, is(notNullValue()));
    }
}
