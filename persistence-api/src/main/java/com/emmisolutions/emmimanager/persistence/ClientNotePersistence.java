package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.ClientNote;

/**
 * ClientNote Persistence
 */
public interface ClientNotePersistence {

    /**
     * Delete existing ClientNote by id
     * 
     * @param id
     *            to delete
     */
    public void delete(Long id);

    /**
     * Find ClientNote by Client
     * 
     * @param client
     *            to use
     * @return null or an existing ClientNote
     */
    public ClientNote findByClientId(Long clientId);

    /**
     * Reload ClientNote by passed in id
     * 
     * @param id
     *            to reload
     * @return an existing ClientNote
     */
    public ClientNote reload(Long id);

    /**
     * Save or update the passed in ClientNote
     * 
     * @param clientNote
     *            to save or update
     * @return the saved or updated ClientNote
     */
    public ClientNote saveOrUpdate(ClientNote clientNote);

}
