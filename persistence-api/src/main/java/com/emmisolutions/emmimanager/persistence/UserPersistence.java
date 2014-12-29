package com.emmisolutions.emmimanager.persistence;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;

/**
 * User persistence interface.
 */
public interface UserPersistence {

    /**
     * Saves or updates the UserAdmin object
     *
     * @param user the user to save or create
     * @return the saved user
     */
    UserAdmin saveOrUpdate(UserAdmin user);

    /**
     * Pulls the User from the database using the login
     *
     * @param login case insensitive search
     * @return User or null if one is not found
     */
    UserAdmin reload(String login);

    /**
     * Retrieves a user by login and eagerly loads all permissions
     *
     * @param login case insensitive search
     * @return a User or null
     */
    UserAdmin fetchUserWillFullPermissions(String login);

    /**
     * Finds a page of Users that are eligible to be contract owners
     * @param pageable the specification to fetch
     * @return a page of User objects
     */
    Page<UserAdmin> listPotentialContractOwners(Pageable pageable);

	/**
     * Find a page of User based on pagable and filter
     *
     * @param pageable contains pageable information
     * @param filter   contains search criteria
     * @return Page<UserAdmin>
     */
	Page<UserAdmin> list(Pageable page, UserAdminSearchFilter filter);

    /**
     * Pulls the User from the database using the id
     *
     * @param id search
     * @return User or null if one is not found
     */
	UserAdmin reload(UserAdmin user);
    
	/**
     * Find all user admin roles excluding the system roles
     *
     * @return PAge<UserAdminRole> or null
     */
	Page<UserAdminRole> listRolesWithoutSystem(Pageable pageable);

    /**
     * Deletes all UserAdminUserAdminRoleRepository for a give userAdmin
     *
     * @param userAdmin to delete
     */
	long removeAllAdminRoleByUserAdmin(UserAdmin user);

	/**
	 * Saves the useradmin useradminrole set
	 * @param userAdminUserAdminRole
	 * @return
	 */
	Set<UserAdminUserAdminRole> saveAll(
			Set<UserAdminUserAdminRole> userAdminUserAdminRole);
}
