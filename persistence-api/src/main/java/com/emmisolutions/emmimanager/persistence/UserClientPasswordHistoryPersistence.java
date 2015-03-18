package com.emmisolutions.emmimanager.persistence;

import java.util.List;

import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;

/**
 * UserClientPasswordHistory Persistence
 */
public interface UserClientPasswordHistoryPersistence {

    /**
     * Delete existing UserClientPasswordHistory by id
     * 
     * @param id
     *            to delete
     */
    public void delete(Long id);

    /**
     * Reload UserClientPasswordHistory by passed in id
     * 
     * @param id
     *            to reload
     * @return an existing UserClientPasswordHistory
     */
    public UserClientPasswordHistory reload(Long id);

    /**
     * Save or update the passed in UserClientPasswordHistory
     * 
     * @param userClientPasswordHistory
     *            to save or update
     * @return the saved or updated UserClientPasswordHistory
     */
    public UserClientPasswordHistory saveOrUpdate(
            UserClientPasswordHistory userClientPasswordHistory);

    public List<UserClientPasswordHistory> findByUserClientId(Long id);
}
