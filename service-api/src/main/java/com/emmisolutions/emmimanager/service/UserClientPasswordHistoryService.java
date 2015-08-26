package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The UserClientPasswordHistory Service
 */
public interface UserClientPasswordHistoryService {

    /**
     * Delete the existing UserClientPasswordHistory
     */
    void delete(UserClientPasswordHistory userClientPasswordHistory);

    /**
     * Get a page of UserClientPasswordHistory by userClientId
     *
     * @param pageable   to use
     * @param userClient to lookup
     * @return a page of UserClientPasswordHistory
     */
    Page<UserClientPasswordHistory> get(Pageable pageable, UserClient userClient);

    /**
     * Reload UserClientPasswordHistory by id
     *
     * @param userClientPasswordHistory to reload
     * @return an existing UserClientPasswordHistory
     */
    UserClientPasswordHistory reload(UserClientPasswordHistory userClientPasswordHistory);

    /**
     * Save a UserClientPasswordHistory
     *
     * @param userClientPasswordHistory to save
     * @return saved UserClientPasswordHistory
     */
    UserClientPasswordHistory save(UserClientPasswordHistory userClientPasswordHistory);

    /**
     * Handle UserClientPasswordHistory for a given UserClient
     *
     * @param userClient to handle
     */
    void handleUserClientPasswordHistory(
            UserClient userClient);
}
