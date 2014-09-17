package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Client persistence class
 */
public interface ClientPersistence {

    /**
     * Fetches a page of Client objects
     *
     * @param page         defines which page or null for the first page
     * @param searchFilter to filter the client list
     * @return a page of client objects
     */
    Page<Client> list(Pageable page, ClientSearchFilter searchFilter);

    /**
     * Saves a client
     *
     * @param client to be saved
     * @return the saved client
     */
    Client save(Client client);

    /**
     * Loads a client based upon an id
     *
     * @param id to load
     * @return the client or null
     */
    Client reload(Long id);

    /**
     * This method will normalize the passed value (toCheck) given the same rules 
     * for the unique client name then do a  simple equality query on the db.. if
     * a match is found, returns the client otherwise null
     * 
     * @param normalizedName
     * @return
     */
    Client findByNormalizedName(String normalizedName);

}
