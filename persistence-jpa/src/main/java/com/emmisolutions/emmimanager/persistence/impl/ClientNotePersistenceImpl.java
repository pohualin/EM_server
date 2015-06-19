package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.ClientNote;
import com.emmisolutions.emmimanager.persistence.ClientNotePersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientNoteRepository;

/**
 * Persistence Implementation to deal with PasswordConfiguration
 */
@Repository
public class ClientNotePersistenceImpl implements ClientNotePersistence {

    @Resource
    ClientNoteRepository clientNoteRepository;

    @Override
    public ClientNote findByClientId(Long clientId) {
        return clientNoteRepository.findByClientId(clientId);
    }

    @Override
    public ClientNote reload(Long id) {
        return clientNoteRepository.findOne(id);
    }

    @Override
    public ClientNote saveOrUpdate(ClientNote clientNote) {
        return clientNoteRepository.save(clientNote);
    }

    @Override
    public void delete(Long id) {
        ClientNote toDelete = reload(id);
        if (toDelete != null) {
            clientNoteRepository.delete(toDelete);
        }
    }

}
