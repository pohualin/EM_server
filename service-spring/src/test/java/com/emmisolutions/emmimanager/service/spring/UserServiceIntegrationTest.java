package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserService;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * An integration test that goes across a wired persistence layer as well
 */
public class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserService userService;

    /**
     * Create without login
     */
    @Test(expected = ConstraintViolationException.class)
    public void testUserCreateWithoutLogin() {
        userService.save(new UserAdmin());
    }

    /**
     * Create with required values
     */
    @Test
    public void testUserCreate() {
        UserAdmin user = new UserAdmin("login", "pw");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user = userService.save(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
    }

    /**
     * Make sure logged in user comes back from service layer utility
     */
    @Test
    public void login(){
        login("super_admin");
        assertThat("super admin is logged in user", userService.loggedIn().getLogin(), is("super_admin"));
    }


    /**
     * Make sure logged in user comes back from service layer utility.
     * Different flows are tested here than the above login test.
     */
    @Test
    public void loginUserDetails(){
        login("super_admin", new ArrayList<GrantedAuthority>());
        assertThat("super admin is logged in user", userService.loggedIn().getLogin(), is("super_admin"));
    }

}
