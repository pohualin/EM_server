package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Client Service API.
 */
public interface ClientService {

    /**
     * Fetch a page of clients
     *
     * @param page         which page to retrieve
     * @param searchFilter filtered by
     * @return a page of clients
     */
    Page<Client> list(Pageable page, ClientSearchFilter searchFilter);

    /**
     * Fetch first page of clients (default page size)
     *
     * @param searchFilter filtered by
     * @return a page of clients
     */
    Page<Client> list(ClientSearchFilter searchFilter);

    /**
     * Reloads a client from persistent storage
     *
     * @param client to reload
     * @return the reloaded client
     */
    Client reload(Client client);

    /**
     * Creates a new client only
     *
     * @param client to be created
     * @return the new client (with id/version)
     */
    Client create(Client client);

    /**
     * Updates an existing client
     *
     * @param client to be updated, must have an id or version
     * @return the updated client
     */
    Client update(Client client);

    /**
     * Finds a page of Users that are eligible to be contract owners
     *
     * @param pageable the specification to fetch
     * @return a page of User objects
     */
    Page<UserAdmin> listPotentialContractOwners(Pageable pageable);

    /**
     * Find a client that are equal to normalizedName
     *
     * @param normalizedName
     * @return a Client object
     */
    Client findByNormalizedName(String normalizedName);

    /**
     * Load all possible client types
     *
     * @return collection of ClientType objects
     */
    Collection<ClientType> getAllClientTypes();

    /**
     * Load all possible client regions
     *
     * @return collection of ClientRegion objects
     */
    Collection<ClientRegion> getAllClientRegions();

    /**
     * Load all possible client tiers
     *
     * @return collection of ClientTier objects
     */
    Collection<ClientTier> getAllClientTiers();
}
