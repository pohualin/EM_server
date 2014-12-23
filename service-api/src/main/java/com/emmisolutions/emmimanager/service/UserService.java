package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.UserSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;

/**
 * The User Service
 */
public interface UserService {

    /**
     * Creates a new user admin or updates an existing user with the passed User object.
     *
     * @param user request to save
     * @return the saved user
     */
    UserAdmin save(UserAdminSaveRequest req);

    /**
     * Fetch the currently logged in user.
     *
     * @return the User
     */
    UserAdmin loggedIn();

    /**
     * Reloads a User from persistent storage
     *
     * @param user to reload
     * @return the reloaded user
     */
	UserAdmin reload(UserAdmin user);
	
    /**
     * Get a page of user objects.
     *
     * @param page             to retrieve
     * @param userSearchFilter filtered by
     * @return a page of users objects
     */
	Page<UserAdmin> list(Pageable page, UserSearchFilter userSearchFilter);

	/**
     * Find all user admin roles excluding the system roles
     *
     * @return PAge<UserAdminRole> or null
     */
	Page<UserAdminRole> listRolesWithoutSystem(Pageable pageable);
}
