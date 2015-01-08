package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermission;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRoleRepository;

/**
 * Integration test for user persistence.
 */
public class UserPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserPersistence userPersistence;
        
    /**
     * Invalid user no login
     */
    @Test(expected = ConstraintViolationException.class)
    public void testConstraints() {
        // user is invalid without a login
        userPersistence.saveOrUpdate(new UserAdmin());
    }

    /**
     * find user admin roles without system role
     */
    @Test
    public void testUserAdminRoles() {
    	
    	Page<UserAdminRole> roles = userPersistence.listRolesWithoutSystem(null);
        assertThat("the search roles return values", roles.getContent(), is(notNullValue()));
        assertThat("the search roles return values", roles.hasContent(), is(true) );

    }
    /**
     * valid create
     */
    @Test
    public void testCreate() {
        login("some user");
        UserAdmin user = new UserAdmin("login", "pw");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user = userPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        UserAdmin user1 = userPersistence.reload(user);
        assertThat("the users saved should be the same as the user fetched", user, is(user1));
        assertThat("auditor is set properly", user.getCreatedBy(), is("some user"));
        
        Page<UserAdminRole> roles = userPersistence.listRolesWithoutSystem(null);
        Set userAdminUserAdminRoles = new HashSet<UserAdminUserAdminRole>();
        for(UserAdminRole role: roles.getContent()){
            UserAdminUserAdminRole uauar = new UserAdminUserAdminRole();
            uauar.setUserAdmin(user1);
            uauar.setUserAdminRole(role);
            userAdminUserAdminRoles.add(uauar);
        }
        userPersistence.saveAll(userAdminUserAdminRoles);
        
        UserAdminSearchFilter filter = new UserAdminSearchFilter(UserAdminSearchFilter.StatusFilter.ALL , "firstName");
        Page<UserAdmin> users = userPersistence.list(null, filter);
        
        assertThat("the search user return values", users.getContent(), is(notNullValue()));
        assertThat("the search user return values", users.hasContent(), is(true) );
        
        UserAdmin findUser = users.getContent().iterator().next();
        assertThat("the users returned is the same user saved", findUser, is(user1));

        logout();
    }
    
    /**
     * save then reloadLocationUsingClient
     */
    @Test
    public void testLoad() {
        String login = "aLogin";
        UserAdmin user = new UserAdmin(login, "pw");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user = userPersistence.saveOrUpdate(user);
        assertThat("the user should get an id after persistence", user.getId(), is(notNullValue()));
        assertThat("the user should get a version after persistence", user.getVersion(), is(notNullValue()));

        user = userPersistence.reload(login);
        assertThat("user is found", user, is(notNullValue()));
        assertThat("username should be lowercase only", user.getLogin(), is(login.toLowerCase()));
    }

    /**
     * make sure super_admin has PERM_GOD
     */
    @Test
    public void proveExistenceOfGod() {
        UserAdmin god = userPersistence.fetchUserWillFullPermissions("super_admin");
        assertThat("Should have one role ", god.getRoles(), is(notNullValue()));
        UserAdminRole role = god.getRoles().iterator().next().getUserAdminRole();
        assertThat("Role is the system role", role.getName(), is("SYSTEM"));
        assertThat("Role is not a default role", role.isDefaultRole(), is(false));

        UserAdminPermission godPermission = new UserAdminPermission();
        godPermission.setName(UserAdminPermissionName.PERM_GOD);
        assertThat("Role has the god permission", role.getPermissions(), hasItem(godPermission));
    }
    
    /**
     * make sure Emmi User Role is default role
     */
    @Test
    public void testEmmiUserIsDefaultRole() {
        UserAdmin god = userPersistence.fetchUserWillFullPermissions("contract_owner2");
        UserAdminRole role = god.getRoles().iterator().next().getUserAdminRole();
        assertThat("Emmi User Role is a default role", role.isDefaultRole(), is(true));
    }

    /**
     * list contract owner data
     */
    @Test
    public void listPotentialContractOwners() {
        UserAdmin contractOwner = userPersistence.reload("contract_owner");
        Page<UserAdmin> ret = userPersistence.listPotentialContractOwners(null);
        assertThat("Users should be returned", ret.hasContent(), is(true));
        assertThat("contract_owner should be in the page", ret.getContent(), hasItem(contractOwner));
    }

}
