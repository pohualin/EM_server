package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration test for user persistence.
 */
public class UserAdminPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserAdminPersistence userAdminPersistence;

    @PersistenceContext
    EntityManager entityManager;
        
    /**
     * Invalid user no login
     */
    @Test(expected = ConstraintViolationException.class)
    public void testConstraints() {
        // user is invalid without a login
        userAdminPersistence.saveOrUpdate(new UserAdmin());
    }

    /**
     * find user admin roles without system role
     */
    @Test
    public void testUserAdminRoles() {
    	
    	Page<UserAdminRole> roles = userAdminPersistence.listRolesWithoutSystem(null);
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
        user = userAdminPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        UserAdmin user1 = userAdminPersistence.reload(user);
        assertThat("the users saved should be the same as the user fetched", user, is(user1));
        assertThat("auditor is set properly", user.getCreatedBy(), is("some user"));
        
        Page<UserAdminRole> roles = userAdminPersistence.listRolesWithoutSystem(null);
        List<UserAdminUserAdminRole> userAdminUserAdminRoles = new ArrayList<>();
        for(UserAdminRole role: roles.getContent()){
            UserAdminUserAdminRole uauar = new UserAdminUserAdminRole();
            uauar.setUserAdmin(user1);
            uauar.setUserAdminRole(role);
            userAdminUserAdminRoles.add(uauar);
        }
        userAdminPersistence.saveAll(userAdminUserAdminRoles);
        
        UserAdminSearchFilter filter = new UserAdminSearchFilter(UserAdminSearchFilter.StatusFilter.ALL , "firstName");
        Page<UserAdmin> users = userAdminPersistence.list(null, filter);
        
        assertThat("the search user return values", users.getContent(), is(notNullValue()));
        assertThat("the search user return values", users.getContent(), hasItem(user) );
        
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
        user = userAdminPersistence.saveOrUpdate(user);
        assertThat("the user should get an id after persistence", user.getId(), is(notNullValue()));
        assertThat("the user should get a version after persistence", user.getVersion(), is(notNullValue()));

        user = userAdminPersistence.reload(login);
        assertThat("user is found", user, is(notNullValue()));
        assertThat("username should be lowercase only", user.getLogin(), is(login.toLowerCase()));
    }

    /**
     * make sure super_admin has PERM_GOD
     */
    @Test
    public void proveExistenceOfGod() {
        UserAdmin god = userAdminPersistence.fetchUserWillFullPermissions("super_admin");
        assertThat("Should have one role ", god.getRoles().size(), is(1));
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
        UserAdmin god = userAdminPersistence.fetchUserWillFullPermissions("contract_owner2");
        UserAdminRole role = god.getRoles().iterator().next().getUserAdminRole();
        assertThat("Emmi User Role is a default role", role.isDefaultRole(), is(true));
    }

    /**
     * list contract owner data
     */
    @Test
    public void listPotentialContractOwners() {
        UserAdmin contractOwner = userAdminPersistence.reload("contract_owner");
        Page<UserAdmin> ret = userAdminPersistence.listPotentialContractOwners(null);
        assertThat("Users should be returned", ret.hasContent(), is(true));
        assertThat("contract_owner should be in the page", ret.getContent(), hasItem(contractOwner));
    }
    
    @Test
    public void findConflicting() {
        UserAdmin userAdminBoss = makeNewRandomUserAdmin();
        userAdminBoss.setEmail("boss@emmi.com");
        userAdminPersistence.saveOrUpdate(userAdminBoss);

        UserAdmin userAdminEmployee = new UserAdmin("login", "password");
        userAdminEmployee.setFirstName("New");
        userAdminEmployee.setLastName("Guy");
        userAdminEmployee.setEmail("boss@emmi.com");
        Set<UserAdmin> conflicts = userAdminPersistence
                .findConflictingUsers(userAdminEmployee);
        assertThat("conflicts include boss", conflicts, hasItem(userAdminBoss));

        Set<UserAdmin> self = userAdminPersistence
                .findConflictingUsers(userAdminBoss);
        assertThat("no conflict", self.size(), is(0));
        
        Set<UserAdmin> noConflict = userAdminPersistence
                .findConflictingUsers(new UserAdmin());
        assertThat("no conflict", noConflict.size(), is(0));
        
        Set<UserAdmin> noConflictA = userAdminPersistence
                .findConflictingUsers(null);
        assertThat("no conflict", noConflictA.size(), is(0));
    }

    @Test
    public void isSystemUser(){
        UserAdmin superUser = userAdminPersistence.fetchUserWillFullPermissions("super_admin");
        assertThat("super user is system user", userAdminPersistence.isSystemUser(superUser), is(true));

        assertThat("new user is not system user", userAdminPersistence.isSystemUser(new UserAdmin()), is(false));

        assertThat("null user is not system user", userAdminPersistence.isSystemUser(null), is(false));

        assertThat("should remove one role", userAdminPersistence.removeAllAdminRoleByUserAdmin(superUser), is(1l));

        entityManager.detach(superUser);

        assertThat("super user is no longer system user", userAdminPersistence.isSystemUser(superUser), is(false));
    }

}
