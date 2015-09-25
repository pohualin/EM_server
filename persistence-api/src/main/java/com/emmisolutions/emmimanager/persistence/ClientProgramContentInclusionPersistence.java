package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;

/**
 * Persistence API for Client Program Content Inclusion.
 */
public interface ClientProgramContentInclusionPersistence {

	/**
     * Delete an ClientProgramContentInclusion
     * 
     * @param id
     *            to delete
     *
     */
    void delete(Long id);
    
	/**
     * Find a page of client program content inclusion
     *
     * @param clientId for which to find the program content inclusion
     * @return a  ClientProgramContentInclusion objects
     */
    Page<ClientProgramContentInclusion> findByClient(Long clientId, Pageable pageable);

    /**
     * Save/Update a client program content inclusion
     *
     * @param clientProgramContentInclusion to be saved
     * @return the saved  ClientProgramContentInclusion object
     */
    ClientProgramContentInclusion saveOrUpdate(ClientProgramContentInclusion clientProgramContentInclusion);

    /**
     * Reloads a ClientProgramContentInclusion from persistence
     *
     * @param id to reload
     * @return the persistent ClientProgramContentInclusion object or null if the ClientProgramContentInclusion is null or not found
     */
    ClientProgramContentInclusion reload(Long id);

}
