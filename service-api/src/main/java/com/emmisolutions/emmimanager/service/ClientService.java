package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Client Service API.
 */
public interface ClientService {

    /**
     * Fetch a page of clients
     *
     * @param page which page to retrieve
     * @return list of clients
     */
    Page<Client> list(Pageable page, Set<String> clientNameFilter, String status);


    /**
     * Reloads a client from persistent storage
     *
     * @param client to reload
     * @return the reloaded client
     */
    Client reload(Client client);

    /**
     * Creates a new client only
     * @param client to be created
     * @return the new client (with id/version)
     */
    Client create(Client client);

    /**
     * Updates an existing client
     * @param client to be updated, must have an id or version
     * @return the updated client
     */
    Client update(Client client);
}
