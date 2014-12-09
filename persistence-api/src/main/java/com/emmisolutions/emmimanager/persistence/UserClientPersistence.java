package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;

/**
 * A sample persistence interface.
 */
public interface UserClientPersistence {

    UserClient reload(Long userClientId);
    
    /**
     * Saves or updates the User object
     *
     * @param user
     *            the user to save or create
     * @return the saved user
     */
    UserClient saveOrUpdate(UserClient user);

    /**
     * Find a page of UserClient based on pagable and filter
     * 
     * @param pageable
     *            contains pageable information
     * @param filter
     *            contains search criteria
     * @return Page<UserClient>
     */
    Page<UserClient> list(Pageable pageable, UserClientSearchFilter filter);

}
