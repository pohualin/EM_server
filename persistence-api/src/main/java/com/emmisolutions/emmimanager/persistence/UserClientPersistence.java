package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Persistence API for UserClient.
 */
public interface UserClientPersistence {

    /**
     * Reload a UserClient with userClientId
     *
     * @param userClientId to use
     * @return the user client
     */
    UserClient reload(UserClient userClientId);

    /**
     * Saves or updates the UserClient object
     *
     * @param user the user to save or create
     * @return the saved user
     */
    UserClient saveOrUpdate(UserClient user);

    /**
     * Find a page of UserClient based on pageable and filter
     *
     * @param pageable contains pageable information
     * @param filter   contains search criteria
     * @return Page<UserClient>
     */
    Page<UserClient> list(Pageable pageable, UserClientSearchFilter filter);
    
    /**
     * Find UserClients that would violate unique constraints
     *
     * @param userClient to find conflicts for
     * @return a set of conflicting user clients
     */
    Set<UserClient> findConflictingUsers(UserClient userClient);

    /**
     * Fetches a UserClient with its full permission set loaded
     *
     * @param currentLogin the case insensitive login field
     * @return a UserClient or null
     */
    UserClient fetchUserWillFullPermissions(String currentLogin);

    /**
     * Finds a UserClient by its activation code
     *
     * @param activationKey to find by
     * @return a UserClient or null
     */
    UserClient findByActivationKey(String activationKey);

    /**
     * Finds a UserClient by its password reset token
     *
     * @param resetToken to find by
     * @return a UserClient or null
     */
    UserClient findByResetToken(String resetToken);

    /**
     * Finds a UserClient by its validation token
     *
     * @param validationToken to find by
     * @return a UserClient or null
     */
    UserClient findByValidationToken(String validationToken);

    /**
     * Finds a user by email
     *
     * @param email to lookup
     * @return the UserClient or null
     */
    UserClient findByEmail(String email);
    
    /**
     * Set account non lock to true, reset login failure count and lock
     * expiration timestamp
     * 
     * @param userClient
     *            to unlock
     * @return an unlocked userClient
     */
    UserClient unlockUserClient(UserClient userClient);
}
