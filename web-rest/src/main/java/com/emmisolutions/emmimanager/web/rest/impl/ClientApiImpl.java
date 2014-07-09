package com.emmisolutions.emmimanager.web.rest.impl;

import com.emmisolutions.emmimanager.api.ClientApi;
import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Implementation of ClientApi
 */
@Component
public class ClientApiImpl implements ClientApi {

    @Resource
    ClientService clientService;

    @Override
    public Page<Client> list(Pageable pageable, String nameFilter, String statusFilter) {
        return clientService.list(pageable);
    }

    @Override
    public Client get(Client client) {
        return clientService.reload(client);
    }
}
