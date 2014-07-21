package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Implementation of the ClientService
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    ClientPersistence clientPersistence;

    @Override
    @Transactional(readOnly = true)
    public Page<Client> list(Pageable pageable, Set<String> clientNameFilter, String statusString) {
        return clientPersistence.list(pageable,
                clientNameFilter,
                ClientPersistence.StatusFilter.fromStringOrAll(statusString));
    }

    @Override
    @Transactional(readOnly = true)
    public Client reload(Client client) {
        return clientPersistence.reload(client);
    }

    @Override
    @Transactional
    public Client create(Client client) {
        client.setId(null);
        client.setVersion(null);
        return clientPersistence.save(client);
    }

    @Override
    @Transactional
    public Client update(Client client) {
        if (client == null || client.getId() == null || client.getVersion() == null){
            throw new IllegalArgumentException("Client Id and Version cannot be null.");
        }
        return clientPersistence.save(client);
    }
}
