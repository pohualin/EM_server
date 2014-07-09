package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Page<Client> list(Pageable page);


    /**
     * Reloads a client from persistent storage
     *
     * @param client to reload
     * @return the reloaded client
     */
    Client reload(Client client);
}
