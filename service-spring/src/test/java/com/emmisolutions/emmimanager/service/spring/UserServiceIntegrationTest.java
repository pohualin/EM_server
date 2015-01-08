package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserService;

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
    	UserAdminSaveRequest req = new UserAdminSaveRequest();
       	req.setUserAdmin(new UserAdmin());
       	req.setRoles(new HashSet<UserAdminRole>());
        UserAdmin user = userService.save(req);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
    }


    /**
     * find user admin roles without system role
     */
    @Test
    public void testUserAdminRoles() {
    	Page<UserAdminRole> roles = userService.listRolesWithoutSystem(null);
        assertThat("the search roles return values", roles.getContent(), is(notNullValue()));
        assertThat("the search roles return values", roles.getContent().size(), is(2) );
    }
    
    /**
     * Create with required values
     */
    @Test
    public void testUserCreate() {
        UserAdmin user = new UserAdmin("login", "pw");
        user.setFirstName("firstName1");
        user.setLastName("lastName");
        
        Page<UserAdminRole> roles = userService.listRolesWithoutSystem(null);
        UserAdminRole role = roles.getContent().iterator().next();
        Set<UserAdminRole> adminRoles = new HashSet<UserAdminRole>();
        adminRoles.add(role);
        
    	UserAdminSaveRequest req = new UserAdminSaveRequest();
       	req.setUserAdmin(user);
       	req.setRoles(adminRoles);
        user = userService.save(req);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
        
        UserAdmin user1 = userService.fetchUserWillFullPermissions(user);
        assertThat("the users saved should be the same as the user fetched", user, is(user1));

        assertThat("the users roles saved", user1.getRoles().size(), is(1));

        user = userService.save(req); //execute again just to verify that is deleting the previous roles
        
        UserAdminSearchFilter filter = new UserAdminSearchFilter(UserAdminSearchFilter.StatusFilter.ALL , "firstName1");
        Page<UserAdmin> users = userService.list(null, filter);
        
        assertThat("the search user return values", users.getContent(), is(notNullValue()));
        assertThat("the search user return values", users.getContent().size(), is(1) );
        
        UserAdmin findUser = users.getContent().iterator().next();
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
