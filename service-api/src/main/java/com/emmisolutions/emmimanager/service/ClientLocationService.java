package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Service API for ClientLocation objects
 */
public interface ClientLocationService {

    /**
     * Find a page of client locations for a client
     *
     * @param client   to find client locations
     * @param pageable which page to fetch
     * @return a page of client location objects
     */
    Page<ClientLocation> find(Client client, Pageable pageable);

    /**
     * Removes a ClientLocation
     *
     * @param toRemove the ClientLocation to delete
     */
    void remove(ClientLocation toRemove);

    /**
     * Create locations for the client. Think of this as two separate calls.. the save, then
     * a find (above)
     *
     * @param client    for which to create them
     * @param locations to associate
     * @param pageable  page to fetch after creation
     * @return a page of persistent locations
     * @see com.emmisolutions.emmimanager.service.ClientLocationService#find(com.emmisolutions.emmimanager.model.Client, org.springframework.data.domain.Pageable)
     */
    Page<ClientLocation> create(Client client, Set<Location> locations, Pageable pageable);
}
