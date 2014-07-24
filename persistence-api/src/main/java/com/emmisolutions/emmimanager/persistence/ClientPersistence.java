package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Client persistence class
 */
public interface ClientPersistence {

    Page<Client> list(Pageable page, ClientSearchFilter searchFilter);

    Client save(Client client);

    Client reload(Client client);

}
