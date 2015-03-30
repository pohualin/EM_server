package com.emmisolutions.emmimanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;

/**
 * The UserClientPasswordHistory Service
 */
public interface UserClientPasswordHistoryService {

    /**
     * Delete the existing UserClientPasswordHistory
     */
    public void delete(UserClientPasswordHistory userClientPasswordHistory);

    /**
     * Get a page of UserClientPasswordHistory by userClientId
     * 
     * @param pageable
     *            to use
     * @param userClient
     *            to lookup
     * @return a page of UserClientPasswordHistory
     */
    public Page<UserClientPasswordHistory> get(Pageable pageable,
            UserClient userClient);

    /**
     * Reload UserClientPasswordHistory by id
     * 
     * @param userClientPasswordHistory
     *            to reload
     * @return an existing UserClientPasswordHistory
     */
    public UserClientPasswordHistory reload(
            UserClientPasswordHistory userClientPasswordHistory);

    /**
     * Save a UserClientPasswordHistory
     * 
     * @param userClientPasswordHistory
     *            to save
     * @return saved UserClientPasswordHistory
     */
    public UserClientPasswordHistory save(
            UserClientPasswordHistory userClientPasswordHistory);

    /**
     * Handle UserClientPasswordHistory for a given UserClient
     * 
     * @param userClient
     *            to handle
     * @return a list of UserClientPasswordHistory
     */
    public List<UserClientPasswordHistory> handleUserClientPasswordHistory(
            UserClient userClient);
}
