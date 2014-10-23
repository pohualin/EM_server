package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Persistence API for ClientLocation objects
 */
public interface ClientLocationPersistence {

    /**
     * Find a page of client locations for a client
     *
     * @param client   to find client locations
     * @param pageable which page to fetch
     * @return a page of client location objects
     */
    Page<ClientLocation> find(Client client, Pageable pageable);

    /**
     * Creates a ClientLocation from a Location and a Client
     * @param location to use
     * @param client to use
     * @return saved ClientLocation
     */
    ClientLocation create(Location location, Client client);

    /**
     * Removes a ClientLocation from the db
     *
     * @param toRemove to delete
     */
    void remove(ClientLocation toRemove);
}
