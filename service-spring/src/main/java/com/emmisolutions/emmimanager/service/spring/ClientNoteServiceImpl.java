package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientNote;
import com.emmisolutions.emmimanager.persistence.ClientNotePersistence;
import com.emmisolutions.emmimanager.service.ClientNoteService;
import com.emmisolutions.emmimanager.service.ClientService;

/**
 * Service Implementation for ClientNote
 */
@Service
public class ClientNoteServiceImpl implements ClientNoteService {

    @Resource
    ClientService clientService;

    @Resource
    ClientNotePersistence clientNotePersistence;

    @Override
    @Transactional
    public void delete(ClientNote clientNote) {
        if (clientNote == null || clientNote.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientNote or clientNoteId cannot be null");
        }
        clientNotePersistence.delete(clientNote.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientNote findByClient(Client client) {
        Client toUse = clientService.reload(client);
        if (toUse != null) {
            return clientNotePersistence.findByClientId(toUse.getId());
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ClientNote reload(ClientNote clientNote) {
        if (clientNote == null || clientNote.getId() == null) {
            return null;
        }
        return clientNotePersistence.reload(clientNote.getId());
    }

    @Override
    @Transactional
    public ClientNote create(ClientNote clientNote) {
        clientNote.setId(null);
        clientNote.setVersion(null);
        // Reload Client
        clientNote.setClient(clientService.reload(clientNote.getClient()));
        return clientNotePersistence.saveOrUpdate(clientNote);
    }

    @Override
    public ClientNote update(ClientNote clientNote) {
        if (clientNote.getId() == null || clientNote.getVersion() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "clientNote id and version can not be null");
        }
        // Reload Client
        clientNote.setClient(clientService.reload(clientNote.getClient()));
        return clientNotePersistence.saveOrUpdate(clientNote);
    }

}
