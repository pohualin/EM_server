package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientSearchFilter;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.GroupService;

/**
 * Implementation of the ClientService
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    UserPersistence userPersistence;
    
    @Resource
    GroupService groupService;

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
    /* We probably have to eager load the groups. The commented code was part of EM-40 where we got the groups to load by forcing it with the iterator (hack). 
        Client ret = clientPersistence.reload(client);
        System.out.println(ret.getGroups().iterator().hasNext());
        return ret;
	*/
        if (client == null || client.getId() == null){
            return null;
        }
        return clientPersistence.reload(client.getId());
    }

    @Override
    @Transactional
    public Client create(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("client cannot be null");
        }
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
    
	@Override
	@Transactional
	public Client createWithGroups(Client client, List<Group> groups) {
		client.setId(null);
		client.setVersion(null);
		client = clientPersistence.save(client);
		if (groups != null && !groups.isEmpty()) {
			List<Group> savedGroups = new ArrayList<Group>();
			for (Group group : groups) {
				group.setClient(client);
				savedGroups.add(groupService.save(group));
			}
			Set<Group> clientGroups = new HashSet<Group>(savedGroups);
			client.setGroups(clientGroups);
		}
		return client;
    }


}
