package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserAdminService;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * An integration test that goes across a wired persistence layer as well
 */
public class UserAdminServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserAdminService userAdminService;

    /**
     * Create without login
     */
    @Test(expected = ConstraintViolationException.class)
    public void testUserCreateWithoutLogin() {
    	UserAdminSaveRequest req = new UserAdminSaveRequest();
       	req.setUserAdmin(new UserAdmin());
       	req.setRoles(new HashSet<UserAdminRole>());
        UserAdmin user = userAdminService.save(req);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
    }


    /**
     * find user admin roles without system role
     */
    @Test
    public void testUserAdminRoles() {
    	Page<UserAdminRole> roles = userAdminService.listRolesWithoutSystem(null);
        assertThat("the search roles return values", roles.getContent(), is(notNullValue()));
        assertThat("the search roles return values", roles.hasContent(), is(true) );
    }
    
    /**
     * Create with required values
     */
    @Test
    public void testUserCreate() {
        UserAdmin user = new UserAdmin("login", "pw");
        user.setFirstName("firstName1");
        user.setLastName("lastName");
        
        Page<UserAdminRole> roles = userAdminService.listRolesWithoutSystem(null);
        UserAdminRole role = roles.getContent().iterator().next();
        Set<UserAdminRole> adminRoles = new HashSet<>();
        adminRoles.add(role);
        
    	UserAdminSaveRequest req = new UserAdminSaveRequest();
       	req.setUserAdmin(user);
       	req.setRoles(adminRoles);
        user = userAdminService.save(req);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
        
        UserAdmin user1 = userAdminService.fetchUserWillFullPermissions(user);
        assertThat("the users saved should be the same as the user fetched", user, is(user1));
        assertThat("the users roles saved", user1.getRoles().iterator().next().getUserAdminRole(), is(role));

        user = userAdminService.save(req); //execute again just to verify that is deleting the previous roles
        
        UserAdminSearchFilter filter = new UserAdminSearchFilter(UserAdminSearchFilter.StatusFilter.ALL , "firstName1");
        Page<UserAdmin> users = userAdminService.list(null, filter);
        
        assertThat("the search user return values", users.getContent(), is(notNullValue()));
        assertThat("the search user return values", users.getContent(), hasItem(user));
        
        UserAdmin findUser = users.getContent().iterator().next();
        assertThat("the users returned is the same user saved", findUser, is(user1));
    }

}
