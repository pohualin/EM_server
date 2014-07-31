package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Implementation of the ClientService
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    UserPersistence userPersistence;

    @Override
    @Transactional(readOnly = true)
    public Page<Client> list(Pageable pageable, ClientSearchFilter searchFilter) {
        return clientPersistence.list(pageable, searchFilter);
    }

    @Override
    public Page<Client> list(ClientSearchFilter searchFilter) {
        return clientPersistence.list(null, searchFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public Client reload(Client client) {
        if (client == null || client.getId() == null){
            return null;
        }
        return clientPersistence.reload(client.getId());
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

    @Override
    @Transactional(readOnly = true)
    public Page<User> listPotentialContractOwners(Pageable pageable) {
        return userPersistence.listPotentialContractOwners(pageable);
    }
}
