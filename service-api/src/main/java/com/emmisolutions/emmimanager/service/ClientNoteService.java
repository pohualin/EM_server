package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientNote;

/**
 * The ClientNote Service
 */
public interface ClientNoteService {

    /**
     * Delete the existing ClientNote
     */
    public void delete(ClientNote clientNote);

    /**
     * Get the ClientNote for a Client
     * 
     * @param client
     *            to lookup
     * @return an existing ClientNote
     */
    public ClientNote findByClient(Client client);

    /**
     * Reload ClientNote by id
     * 
     * @param clientNote
     *            to reload
     * @return an existing ClientNote
     */
    public ClientNote reload(ClientNote clientNote);

    /**
     * Create a ClientNote
     * 
     * @param clientNote
     *            to create
     * @return saved ClientNote
     */
    public ClientNote create(ClientNote clientNote);

    /**
     * Update a ClientNote
     * 
     * @param clientNote
     *            to create
     * @return saved ClientNote
     */
    public ClientNote update(ClientNote clientNote);
}
