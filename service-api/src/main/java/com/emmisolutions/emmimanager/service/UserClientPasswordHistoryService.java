package com.emmisolutions.emmimanager.service;

import java.util.List;

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
     * Get the UserClientPasswordHistory for a UserClient
     *
     * @param userClient
     *            to lookup
     * @return an existing list of UserClientPasswordHistory
     * 
     */
    public List<UserClientPasswordHistory> get(UserClient userClient);

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
     *            to deal with
     */
    public void handleUserClientPasswordHistory(UserClient userClient);
}
