package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.UserSearchFilter;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserService;

import org.junit.Test;
import org.springframework.data.domain.Page;
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
     * Create with required values
     */
    @Test
    public void testSimpleUserCreate() {
        User user = new User();
        user.setLogin("claudio");
        user.setPassword("clave");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user = userService.create(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
        
        User user1 = userService.reload(user);
        assertThat("the users saved should be the same as the user fetched", user, is(user1));
     
        UserSearchFilter filter = new UserSearchFilter(UserSearchFilter.StatusFilter.ALL , "claudio");
        Page<User> users = userService.list(null, filter);
        
        assertThat("the search user return values", users.getContent(), is(notNullValue()));
        assertThat("the search user return values", users.getContent().size(), is(1) );
        
        User findUser = users.getContent().iterator().next();
        assertThat("the users returned is the same user saved", findUser, is(user1));

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
