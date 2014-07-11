package com.emmisolutions.emmimanager.api;

import com.emmisolutions.emmimanager.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Client API accessible from the web.
 */
public interface ClientApi {

    Page<Client> list(Pageable pageable, String nameFilter, String statusFilter);

    Client get(Client client);

    Client create(Client client);
}
