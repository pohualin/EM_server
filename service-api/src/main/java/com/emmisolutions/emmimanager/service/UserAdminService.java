package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The User Service
 */
public interface UserAdminService {

    /**
     * Creates a new user admin or updates an existing user with the passed User object.
     *
     * @param req request to save
     * @return the saved user
     */
    UserAdmin save(UserAdminSaveRequest req);

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
    Page<UserAdmin> list(Pageable page, UserAdminSearchFilter userSearchFilter);

    /**
     * Find all user admin roles excluding the system roles
     *
     * @return PAge<UserAdminRole> or null
     */
    Page<UserAdminRole> listRolesWithoutSystem(Pageable pageable);

    /**
     * Reloads a User from persistent storage with full roles and permissions
     *
     * @param user to reload
     * @return the reloaded user
     */
    UserAdmin fetchUserWillFullPermissions(UserAdmin user);

    /**
     * Find a list of conflicting UserAdmin
     *
     * @param userAdmin to find
     * @return a list of conflicting UserAdmin
     */
    List<UserAdmin> findConflictingUsers(UserAdmin userAdmin);

    /**
     * Updates the user admin's password
     *
     * @param userAdmin on which to update the password to whatever is in this object
     * @return the updated UserAdmin
     */
    UserAdmin updatePassword(UserAdmin userAdmin);
}
