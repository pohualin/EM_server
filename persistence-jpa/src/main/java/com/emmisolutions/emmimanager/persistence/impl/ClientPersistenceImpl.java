package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Set;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Client persistence implementation.
 */
@Repository
public class ClientPersistenceImpl implements ClientPersistence {

    @Resource
    ClientRepository clientRepository;

    @Override
    public Page<Client> list(Pageable page, Set<String> clientNameFilter, StatusFilter status) {
        Specification<Client> hasNames = ClientSpecifications.hasNames(clientNameFilter);
        Specification<Client> isInStatus = ClientSpecifications.isInStatus(status);
        Specifications<Client> fullSpec = null;

        if (hasNames != null && isInStatus != null) {
            fullSpec = where(hasNames).and(isInStatus);
        } else if (hasNames != null) {
            fullSpec = where(hasNames);
        } else if (isInStatus != null) {
            fullSpec = where(isInStatus);
        }
        if (page == null){
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return clientRepository.findAll(fullSpec, page);
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client reload(Client client) {
        if (client == null || client.getId() == null){
            return null;
        }
        return clientRepository.findOne(client.getId());
    }
}
